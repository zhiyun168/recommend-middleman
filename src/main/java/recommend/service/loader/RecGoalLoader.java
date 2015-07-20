package recommend.service.loader;

import com.google.common.base.Preconditions;
import com.zhiyun168.service.api.goal.IGoalService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.redis.RedisTemplatePlus;
import recommend.service.SearchClientService;
import recommend.utils.CacheKeyHelper;
import recommend.utils.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class RecGoalLoader {

    private static Logger log = LoggerFactory.getLogger(RecGoalLoader.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IGoalService goalService;
    @Autowired
    private SearchClientService searchClientService;


    private static int TIMEOUT = 7;
    private static String INDEX = "recommendation";
    private static String Type = "genderGoal";



    public void deleteCandidates(Long uid, Long recId)
    {
        String recKey = CacheKeyHelper.recGoalKey(uid);
        stringRedisTemplate.opsForList().remove(recKey, 0, recId.toString());
    }

    /**
     * 用户的推荐任务是否加载到cache
     * @param uid
     * @return
     */
    public boolean hasLoadToCache(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");
        String recGoalKey = CacheKeyHelper.recGoalKey(uid);

        return stringRedisTemplate.opsForHash().hasKey(CacheKeyHelper.REC_GOAL_LOAD_KEY, uid.toString())
                && stringRedisTemplate.hasKey(recGoalKey);
    }

    /**
     *加载用户的推荐任务进cache
     * @param uid
     */
    public boolean loadToCache(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");

        String lockKey = CacheKeyHelper.recGoalLockKey(uid);
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

                //过滤推荐任务
                List<String> filtratedRec = filter(rec, uid);

                if(!filtratedRec.isEmpty())
                {
                    String recKey = CacheKeyHelper.recGoalKey(uid);
                    String tmpRecKey = CacheKeyHelper.recTmpGoalKey(uid);
                    stringRedisTemplate.opsForList().rightPushAll(tmpRecKey, filtratedRec);
                    stringRedisTemplate.expire(tmpRecKey, TIMEOUT, TimeUnit.DAYS);
                    stringRedisTemplate.rename(tmpRecKey, recKey);
                }

                stringRedisTemplate.opsForHash().put(CacheKeyHelper.REC_GOAL_LOAD_KEY,
                        uid.toString(), loadTime);

                ok = true;
            }
            catch (Exception e)
            {
                String msg= StringHelper.DotJoiner.join("加载用户的推荐任务失败", uid);
                log.error(msg, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }

        return ok;
    }


    public List<String> filter(List<String> rec, Long uid)
    {
        //return  rec;

        //过滤推荐任务
        List<String> filtratedRec = new ArrayList<>();
        for(String recId: rec)
        {
            Long rec_id = Long.parseLong(recId);
            Boolean isJoin = goalService.isJoinGoal(rec_id, uid);
            if(isJoin!=null && !isJoin)
                filtratedRec.add(recId);
        }
        return  filtratedRec;

    }


    /**
     *从存储推荐信息的源获取所有推荐任务
     * @param uid
     * @return
     */
    public List<String> getCandidatesFromStorage(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");
        Client client = searchClientService.getSearchClient();
        QueryBuilder userQuery = QueryBuilders.termQuery("user", uid.toString());
        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(Type).setQuery(userQuery)
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
