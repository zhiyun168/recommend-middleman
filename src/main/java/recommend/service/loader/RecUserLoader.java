package recommend.service.loader;

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
import recommend.service.SearchClientService;
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
public class RecUserLoader extends  Loader{
    private static Logger log = LoggerFactory.getLogger(RecUserLoader.class);


    @Autowired
    private IUserService userService;


    private static String ES_Type = "alsoFollowing";
    private static int MAX_CARD_COUNT = 3;


    @Override
    public String recKey(Long uid) {
        return CacheKeyHelper.recUserKey(uid);
    }

    @Override
    public String recTmpKey(Long uid) {
        return CacheKeyHelper.recTmpUserKey(uid);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long uid) {
        return CacheKeyHelper.recLoadLockKey(uid);
    }

    @Override
    protected String getEsType() {
        return ES_Type;
    }


    @Override
    public List<String> filter(List<String> rec, Long uid) {
        //过滤推荐用户
        List<String> filtratedRec = new ArrayList<>();
        for(String recUid: rec)
        {
            Long rec_id = Long.parseLong(recUid);
            Long cardCount = userService.getUserCardsCount(rec_id);
            if(cardCount!=null && cardCount >= MAX_CARD_COUNT)
                filtratedRec.add(recUid);
        }
        return filtratedRec;
    }

    @Override
    protected void afterLoad(Long uid) {
        //把加载期间已经关注的用户从cache删除
        DeleteTask deleteTask = new DeleteTask(uid);
        Thread deleteThread = new Thread(deleteTask);
        deleteThread.start();
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
