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

    public abstract String recKey(Long id);
    public abstract String recTmpKey(Long id);
    public abstract String recLoadKey();
    public abstract String recLockKey(Long id);
    public abstract String getEsType();
    public abstract String getEsIdField();

    public String getEsIndexName(){
        return "recommendation";
    }



    private static int TIMEOUT = 7;

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
    public void deleteCandidates(Long id, Long recId)
    {
        String recKey = recKey(id);
        stringRedisTemplate.opsForList().remove(recKey, 0, recId.toString());
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
    public  List<String> filter(List<String> rec, Long id)
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
        String recKey = recKey(id);
        String loadKey = recLoadKey();

        return stringRedisTemplate.opsForHash().hasKey(loadKey, id.toString())
                && stringRedisTemplate.hasKey(recKey);
    }

    /**
     *加载推荐目标id的推荐任务进cache
     * @param id
     */
    public boolean loadToCache(Long id)
    {
        Preconditions.checkNotNull(id, "id不可空");

        String lockKey = recLockKey(id);
        String loadTime = String.valueOf(System.currentTimeMillis());

        boolean ok=false;
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, loadTime, "NX", "EX", 60);

        if(isLock)
        {

            //获取加载锁，从es加载进redis
            try {
                //log.info("start to load:" + uid.toString());
                List<String> rec = getCandidatesFromStorage(id);

                //过滤推荐
                List<String> filtratedRec = filter(rec, id);

                String recKey = recKey(id);
                if(!filtratedRec.isEmpty())
                {
                    String tmpRecKey = recTmpKey(id);
                    stringRedisTemplate.opsForList().rightPushAll(tmpRecKey, filtratedRec);
                    stringRedisTemplate.expire(tmpRecKey, TIMEOUT, TimeUnit.DAYS);
                    stringRedisTemplate.rename(tmpRecKey, recKey);
                }
                else {
                    stringRedisTemplate.delete(recKey);
                }

                stringRedisTemplate.opsForHash().put(recLoadKey(),
                        id.toString(), loadTime);

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
    public List<String> getCandidatesFromStorage(Long id)
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
