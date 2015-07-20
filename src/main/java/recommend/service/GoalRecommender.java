package recommend.service;

import com.zhiyun168.service.api.recommend.IGoalRecommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.service.loader.RecGoalLoader;
import recommend.utils.CacheKeyHelper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class GoalRecommender implements IGoalRecommender {

    @Autowired
    private RecGoalLoader recGoalLoader;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    Random random = new Random();

    /**
     * 获取推荐用户
     * @param uid
     * @return
     */
    @Override
    public List<String> getCandidates(Long uid, int maxSize)
    {
        if(recGoalLoader.hasLoadToCache(uid))
        {
            return loadRandomFromCache(uid, maxSize);
        }
        else
        {
            boolean hasLoad = recGoalLoader.loadToCache(uid);
            if(hasLoad)
            {
                //cache里读
                return loadRandomFromCache(uid, maxSize);
            }
        }
        return loadRandomFromStorage(uid, maxSize);
    }


    private List<String> loadRandomFromCache(Long uid, int maxSie)
    {
        String recKey = CacheKeyHelper.recGoalKey(uid);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recKey);
        int len = ops.size().intValue();
        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return ops.range(0, -1);

        int offset = random.nextInt(len-maxSie+1);

        return ops.range(offset, offset + maxSie-1 );
    }

    private List<String> loadRandomFromStorage(Long uid, int maxSie)
    {
        List<String> rec = recGoalLoader.getCandidatesFromStorage(uid);

        //List<String> filtratedRec = recUser;

        //过滤推荐任务
        List<String> filtratedRec = recGoalLoader.filter(rec,uid);

        int len = filtratedRec.size();

        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return filtratedRec.subList(0, len);

        int offset = random.nextInt(len - maxSie + 1);
        return filtratedRec.subList(offset, offset+maxSie);
    }

}
