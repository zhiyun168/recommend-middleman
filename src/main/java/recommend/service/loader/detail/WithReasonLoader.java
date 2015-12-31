package recommend.service.loader.detail;

import com.google.common.base.Preconditions;
import com.zhiyun168.model.recommend.Candidate;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import recommend.redis.RedisTemplatePlus;
import recommend.service.SearchClientService;
import recommend.service.loader.IBaseLoader;
import recommend.service.loader.KeyBuilder;
import recommend.utils.StringHelper;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ouduobiao on 15/7/20.
 */
public abstract class WithReasonLoader extends KeyBuilder implements ApplicationContextAware, IBaseLoader {

    protected static Logger log = LoggerFactory.getLogger(WithReasonLoader.class);
    private static int TIMEOUT = 7;

    protected StringRedisTemplate stringRedisTemplate;
    protected SearchClientService searchClientService;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        this.searchClientService = applicationContext.getBean(SearchClientService.class);
    }

    /**
     * 从id的候选集合删除推荐recId
     * @param id
     * @param recId
     */
    @Override
    public void deleteCandidates(Long id, Long recId)
    {
        String recKey = recKey(id);
        stringRedisTemplate.opsForList().remove(recKey, 0, recId.toString());
        //没必要删除推荐理由
    }


    public void deleteCandidatesExt(Long id, Long recId)
    {
        if(this.hasLoadToCache(id))
        {
            this.deleteCandidates(id, recId);
        }
        else
        {
            boolean hasLoad = this.loadToCache(id);
            if(hasLoad)//
            {
                this.deleteCandidates(id,recId);
            }
        }
    }




    /**
     * 过滤候选集
     * @param rec
     * @param id
     * @return
     */
    public  Candidate filter(Candidate rec, Long id)
    {
        return rec;
    }

    /**
     * 加载id的候选集后执行的操作
     * @param id
     */
    protected void afterLoad(Long id){};


    /**
     * 推荐目标id的推荐 是否加载到cache
     * @param id
     * @return
     */
    public boolean hasLoadToCache(Long id)
    {
        Preconditions.checkNotNull(id, "id不可空");
        String loadKey = recLoadKey();
        String loadTimeStr = (String)stringRedisTemplate.opsForHash().get(loadKey, id.toString());

        if(loadTimeStr == null)//没有load key
            return false;
        else
        {
            long now = System.currentTimeMillis();
            long endTime;
            try
            {
                endTime = Long.parseLong(loadTimeStr) + TimeoutUtils.toMillis(TIMEOUT, TimeUnit.DAYS);
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
    public boolean loadToCache(final Long id)
    {
        Preconditions.checkNotNull(id, "id不可空");

        String lockKey = recLoadLockKey(id);
        final String loadTime = String.valueOf(System.currentTimeMillis());

        boolean ok=false;
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, loadTime, "NX", "EX", 60);

        if(isLock)
        {

            //获取加载锁，从es加载进redis
            try {
                //log.info("start to load:" + uid.toString());
                Candidate recWithReason = getCandidatesFromStorage(id);

                //过滤推荐
                final Candidate filtratedRec = filter(recWithReason, id);

                stringRedisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection conn = (StringRedisConnection) connection;
                        String recKey = recKey(id);
                        String recReasonKey = recReasonKey(id);
                        List<String> items = filtratedRec.getItems();
                        if(!items.isEmpty())
                        {
                            long exp = TimeoutUtils.toMillis(TIMEOUT, TimeUnit.DAYS);

                            String tmpRecKey = recTmpKey(id);
                            conn.rPush(tmpRecKey, items.toArray(new String[items.size()]));
                            conn.pExpire(tmpRecKey, exp);
                            conn.rename(tmpRecKey, recKey);

                            String tmpRecReasonKey = recReasonTmpKey(id);
                            conn.hMSet(tmpRecReasonKey, filtratedRec.getItemReason());
                            conn.pExpire(tmpRecReasonKey, exp);
                            conn.rename(tmpRecReasonKey, recReasonKey);
                        }
                        else {
                            conn.del(recKey, recReasonKey);
                        }

                        conn.hSet(recLoadKey(), id.toString(), loadTime);
                        return null;
                    }
                },true,true);

                afterLoad(id);

                ok = true;
            }
            catch (Exception e)
            {
                String msg= StringHelper.DotJoiner.join("加载推荐失败", id);
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
     * @param id
     * @return
     */
    public Candidate getCandidatesFromStorage(Long id)
    {
        Preconditions.checkNotNull(id, "id不可空");
        Client client = searchClientService.getSearchClient();
        QueryBuilder userQuery = QueryBuilders.termQuery(getEsIdField(), id.toString());
        SearchResponse response = client.prepareSearch(getEsIndexName())
                .setTypes(getEsType()).setQuery(userQuery)
                .setFrom(0)
                .setSize(1)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();

        if(hitList.length > 0)
        {
            List<String> res = (List<String>)hitList[0].getSource().get("candidates");
            if(res == null)
            {
                return new Candidate();
            }
            else
            {
                List<String> items = new ArrayList<>(res.size());
                Map<String,String> itemReason = new HashMap<>(res.size());
                for(String item_reason : res)
                {
                    String[] split = item_reason.split(":",2);
                    try {
                        String item = split[0].trim();
                        String reason = split[1].trim();
                        items.add(item);
                        itemReason.put(item, reason);
                    }
                    catch (Exception e)
                    {
                        log.error("获取推荐Item与Reason失败", e);
                    }
                }
                return new Candidate(items, itemReason);
            }

        }
        else
            return new Candidate();
    }
}
