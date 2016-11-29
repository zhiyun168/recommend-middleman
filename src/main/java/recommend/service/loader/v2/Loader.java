package recommend.service.loader.v2;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ZSetOperations;
import recommend.model.RecItem;
import recommend.redis.RedisTemplatePlus;
import recommend.utils.Constant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by ouduobiao on 15/7/20.
 */
public abstract class Loader extends KeyBuilder implements ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(Loader.class);

    protected StringRedisTemplate stringRedisTemplate;

    /**
     *从存储推荐信息的源获取所有推荐
     * @param id
     * @return
     */
    abstract public List<RecItem> getCandidatesFromStorage(String id);


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    /**
     * 从id的候选集合删除推荐recId
     * @param id
     * @param recIds
     */
    public void deleteCandidates(String id, String... recIds)
    {
        try {
            String recKey = recKey(id);
            stringRedisTemplate.opsForZSet().remove(recKey, recIds);
        }
        catch (Exception e)
        {
            log.error("从id的候选集合删除推荐recId失败", e);
        }

    }





    /**
     * 推荐目标id的推荐 是否加载到cache
     * @param id
     * @return
     */
    public boolean hasLoadToCache(String id)
    {
        Preconditions.checkNotNull(id, "id不可空");
        String loadKey = recLoadKey();
        String loadTimeStr = (String)stringRedisTemplate.opsForHash().get(loadKey, id);

        if(loadTimeStr == null)//没有load key
            return false;
        else
        {
            long now = System.currentTimeMillis();
            long endTime;
            try
            {
                endTime = Long.parseLong(loadTimeStr) + TimeoutUtils.toMillis(Constant.User_Rec_Timeout_In_Days,
                        TimeUnit.DAYS);
            }
            catch (Exception e)
            {
                log.error("获取时效时间失败", e);
                return false;
            }

            if(now > endTime)//超过时效时间
                return false;
            else {
                return true;
            }
        }
    }

    /**
     *加载推荐目标id的推荐任务进cache
     * @param id
     */
    public boolean loadToCache(String id)
    {
        Preconditions.checkNotNull(id, "id不可空");

        String lockKey = recLoadLockKey(id);
        String loadTime = String.valueOf(System.currentTimeMillis());

        boolean ok=false;
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, loadTime, "NX", "EX", 60);

        if(isLock)
        {

            //获取加载锁，从持久存储加载进redis
            try {
                //log.info("start to load:" + uid.toString());
                List<RecItem> recItems = getCandidatesFromStorage(id);

                String recKey = recKey(id);
                if(!recItems.isEmpty())
                {
                    Set<ZSetOperations.TypedTuple<String>> items = new HashSet<>(recItems.size());
                    for(RecItem item: recItems)
                    {
                        items.add(new DefaultTypedTuple<>(item.getCandidate(), item.getScore()));
                    }

                    String tmpRecKey = recTmpKey(id);
                    stringRedisTemplate.opsForZSet().add(tmpRecKey, items);
                    stringRedisTemplate.expire(tmpRecKey, Constant.User_Rec_Timeout_In_Days, TimeUnit.DAYS);
                    stringRedisTemplate.rename(tmpRecKey, recKey);
                }
                else {
                    stringRedisTemplate.delete(recKey);
                }

                stringRedisTemplate.opsForHash().put(recLoadKey(),
                        id, loadTime);

                ok = true;
            }
            catch (Exception e)
            {
                log.error("加载推荐失败:"+id, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }

        return ok;
    }

}
