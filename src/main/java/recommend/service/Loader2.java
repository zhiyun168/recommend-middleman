package recommend.service;

import com.google.common.base.Preconditions;
import com.zhiyun168.service.api.IUserService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.redis.RedisTemplatePlus;
import recommend.utils.CacheKeyHelper;
import recommend.utils.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by ouduobiao on 15/6/23.
 */
@Service
public class Loader2 {
    private static Logger log = LoggerFactory.getLogger(Loader2.class);

    @Autowired
    private SearchClientService searchClientService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IUserService userService;


    private static String INDEX = "recommendation";
    private static String Type = "alsoFollowing";
    private static int TIMEOUT = 7;
    private static int MAX_CARD_COUNT = 3;


    /**
     *加载用户的推荐列表进cache
     * @param uid
     */
    public boolean loadToCache(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");

        String lockKey = CacheKeyHelper.recLoadLockKey(uid);
        String loadTime = String.valueOf(System.currentTimeMillis());

        boolean ok=false;
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, loadTime, "NX", "EX", 60);

        if(isLock)
        {

            //获取加载锁，从es加载进redis
            try {
                log.info("start to load:" + uid.toString());
                List<String> rec = getRecUserFromStorage(uid);

                List<String> filtratedRec =rec;

                /*
                //过滤推荐用户
                List<String> filtratedRec = new ArrayList<>();
                for(String recUid: rec)
                {
                    if(userService.getUserCardsCount(Long.parseLong(recUid)) >= MAX_CARD_COUNT)
                        filtratedRec.add(recUid);
                }
                */

                if(!filtratedRec.isEmpty())
                {
                    String recUserKey = CacheKeyHelper.recUserKey(uid);
                    String tmpREcUserKey = CacheKeyHelper.recTmpUserKey(uid);
                    stringRedisTemplate.opsForList().rightPushAll(tmpREcUserKey, filtratedRec);
                    stringRedisTemplate.expire(tmpREcUserKey, TIMEOUT, TimeUnit.DAYS);
                    stringRedisTemplate.rename(tmpREcUserKey, recUserKey);
                }

                stringRedisTemplate.opsForHash().put(CacheKeyHelper.REC_LOAD_KEY,
                        uid.toString(), loadTime);

                //把加载期间已经关注的用户从cache删除
                DeleteTask deleteTask = new DeleteTask(uid);
                Thread deleteThread = new Thread(deleteTask);
                deleteThread.start();

                ok = true;
            }
            catch (Exception e)
            {
                String msg=StringHelper.DotJoiner.join("加载用户的推荐用户失败", uid);
                log.error(msg, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }

        return ok;
    }


    /**
     *从存储推荐信息的源获取所有推荐用户
     * @param uid
     * @return
     */
    public List<String> getRecUserFromStorage(Long uid)
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
            return (List<String>)hitList[0].getSource().get("candidates");
        else
            return Collections.EMPTY_LIST;
    }


    /**
     * 用户的推荐用户是否加载到cache
     * @param uid
     * @return
     */
    public boolean hasLoadToCache(Long uid)
    {
        Preconditions.checkNotNull(uid, "uid不可空");
        String recUserKey = CacheKeyHelper.recUserKey(uid);

        return stringRedisTemplate.opsForHash().hasKey(CacheKeyHelper.REC_LOAD_KEY, uid.toString())
                && stringRedisTemplate.hasKey(recUserKey);
    }

    /**
     * 从用户的推荐用户列表中删除指定用户
     * @param uid
     * @param recUid
     */
    public void deleteRecUser(Long uid, Long recUid)
    {
        String recUserKey = CacheKeyHelper.recUserKey(uid);
        stringRedisTemplate.opsForList().remove(recUserKey, 0, recUid.toString());
    }

    public void addFollowedRecUser(Long uid, Long followedUid)
    {
        String followedKey = CacheKeyHelper.recFollowedUser(uid);
        stringRedisTemplate.opsForList().rightPush(followedKey, followedUid.toString());
    }


    private class DeleteTask implements Runnable
    {
        private Long uid;

        public DeleteTask(Long uid) {
            this.uid = uid;
        }

        @Override
        public void run() {
            String followedKey = CacheKeyHelper.recFollowedUser(uid);
            String recKey = CacheKeyHelper.recUserKey(uid);
            ListOperations<String, String> ops = stringRedisTemplate.opsForList();
            List<String> followed = ops.range(followedKey, 0, -1);
            for(String followedId : followed)
            {
                ops.remove(recKey, 0, followedId);
            }
            stringRedisTemplate.delete(followedKey);
        }
    }


}
