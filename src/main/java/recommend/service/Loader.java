package recommend.service;

import com.google.common.base.Preconditions;
import com.zhiyun168.service.api.IUserService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.redisson.Redisson;
import org.redisson.core.RList;
import org.redisson.core.RLock;
import org.redisson.core.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class Loader {
    private static Logger log = LoggerFactory.getLogger(Loader.class);

    @Autowired
    private SearchClientService searchClientService;
    @Autowired
    private Redisson redisson;
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

        final String lockKey = CacheKeyHelper.recLoadLockKey(uid);
        String loadTime = String.valueOf(System.currentTimeMillis());

        RLock lock = redisson.getLock(lockKey);

        boolean ok=false;
        boolean isLock;

        try {
            isLock = lock.tryLock(0, 60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            log.error("获取加载lock失败", e);
            return ok;
        }

        if(isLock)
        {
            //获取加载锁，从es加载进redis
            try {
                List<String> rec = getRecUserFromStorage(uid);

                //List<String> filtratedRec =rec;

                //过滤推荐用户
                List<String> filtratedRec = new ArrayList<>();
                for(String recUid: rec)
                {
                    if(userService.getUserCardsCount(Long.parseLong(recUid)) >= MAX_CARD_COUNT)
                        filtratedRec.add(recUid);
                }


                String recUserKey = CacheKeyHelper.recUserKey(uid);
                RList<String> recUser = redisson.getList(recUserKey);

                recUser.clear();
                recUser.addAll(filtratedRec);
                recUser.expire(TIMEOUT, TimeUnit.DAYS);

                RMap<String, String> loadMap = redisson.getMap(CacheKeyHelper.REC_LOAD_KEY);
                loadMap.put(uid.toString(), loadTime);

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
                lock.unlock();
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
        RList<String> recUser = redisson.getList(recUserKey);

        RMap<String, String> loadMap = redisson.getMap(CacheKeyHelper.REC_LOAD_KEY);

        return loadMap.containsKey(uid.toString()) && recUser.remainTimeToLive() != -2;
    }

    /**
     * 从用户的推荐用户列表中删除指定用户
     * @param uid
     * @param recUid
     */
    public void deleteRecUser(Long uid, Long recUid)
    {
        String recUserKey = CacheKeyHelper.recUserKey(uid);
        RList<String> recUser = redisson.getList(recUserKey);
        recUser.remove(recUid.toString());
    }

    public void addFollowedRecUser(Long uid, Long followedUid)
    {
        String followedKey = CacheKeyHelper.recFollowedUser(uid);
        RList<String> followed = redisson.getList(followedKey);
        followed.add(followedUid.toString());
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

            RList<String> followed = redisson.getList(followedKey);
            RList<String> recUser = redisson.getList(recKey);
            for(String delId : followed)
            {
                recUser.remove(delId);
            }
        }
    }


}
