package recommend.service.loader;

import com.google.common.base.Preconditions;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import recommend.redis.RedisTemplatePlus;
import recommend.service.SearchClientService;
import recommend.utils.StringHelper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ouduobiao on 15/7/20.
 */
public abstract class Loader implements ApplicationContextAware {

    protected static Logger log = LoggerFactory.getLogger(Loader.class);

    protected StringRedisTemplate stringRedisTemplate;

    protected SearchClientService searchClientService;

    public abstract String recKey(Long uid);
    public abstract String recTmpKey(Long uid);
    public abstract String recLoadKey();
    public abstract String recLockKey(Long uid);
    public abstract String getEsType();

    private static String INDEX = "recommendation";
    private static int TIMEOUT = 7;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        this.searchClientService = applicationContext.getBean(SearchClientService.class);
    }

    public void deleteCandidates(Long uid, Long recId)
    {
        String recKey = recKey(uid);
        stringRedisTemplate.opsForList().remove(recKey, 0, recId.toString());
    }

    public  List<String> filter(List<String> rec, Long uid)
    {
        return rec;
    }


    protected void afterLoad(Long uid){};

    /**
     * 用户的推荐 是否加载到cache
     * @param uid
     * @return
     */
    public boolean hasLoadToCache(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");
        String recKey = recKey(uid);
        String loadKey = recLoadKey();

        return stringRedisTemplate.opsForHash().hasKey(loadKey, uid.toString())
                && stringRedisTemplate.hasKey(recKey);
    }

    /**
     *加载用户的推荐任务进cache
     * @param uid
     */
    public boolean loadToCache(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");

        String lockKey = recLockKey(uid);
        String loadTime = String.valueOf(System.currentTimeMillis());

        boolean ok=false;
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, loadTime, "NX", "EX", 60);

        if(isLock)
        {

            //获取加载锁，从es加载进redis
            try {
                //log.info("start to load:" + uid.toString());
                List<String> rec = getCandidatesFromStorage(uid);

                //过滤推荐
                List<String> filtratedRec = filter(rec, uid);

                if(!filtratedRec.isEmpty())
                {
                    String recKey = recKey(uid);
                    String tmpRecKey = recTmpKey(uid);
                    stringRedisTemplate.opsForList().rightPushAll(tmpRecKey, filtratedRec);
                    stringRedisTemplate.expire(tmpRecKey, TIMEOUT, TimeUnit.DAYS);
                    stringRedisTemplate.rename(tmpRecKey, recKey);
                }

                stringRedisTemplate.opsForHash().put(recLoadKey(),
                        uid.toString(), loadTime);

                afterLoad(uid);

                ok = true;
            }
            catch (Exception e)
            {
                String msg= StringHelper.DotJoiner.join("加载用户的推荐失败", uid);
                log.error(msg, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }

        return ok;
    }


    /**
     *从存储推荐信息的源获取所有推荐
     * @param uid
     * @return
     */
    public List<String> getCandidatesFromStorage(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");
        Client client = searchClientService.getSearchClient();
        QueryBuilder userQuery = QueryBuilders.termQuery("user", uid.toString());
        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(getEsType()).setQuery(userQuery)
                .setFrom(0)
                .setSize(1)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();

        if(hitList.length > 0)
        {
            Object res = hitList[0].getSource().get("candidates");
            if(res == null)
                return Collections.EMPTY_LIST;
            else
                return (List<String>)res;
        }
        else
            return Collections.EMPTY_LIST;
    }


}
