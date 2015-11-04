package recommend.service.recommender;

import com.zhiyun168.service.api.recommend.IGoalRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.service.logger.RecommendFeedbackLogger;
import recommend.service.loader.Loader;


import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class GoalRecommender implements IGoalRecommender {

    private static Logger log = LoggerFactory.getLogger(GoalRecommender.class);

    @Autowired
    @Qualifier("recGoalLoader")
    private Loader loader;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RecommendFeedbackLogger recommendFeedbackLogger;

    Random random = new Random();

    /**
     * 获取推荐用户
     * @param uid
     * @return
     */
    @Override
    public List<String> getCandidates(Long uid, int maxSize)
    {
        List<String> candidate ;

        if(loader.hasLoadToCache(uid))
        {
            candidate = loadRandomFromCache(uid, maxSize);
        }
        else
        {
            boolean hasLoad = loader.loadToCache(uid);
            if(hasLoad)
            {
                //cache里读
                candidate = loadRandomFromCache(uid, maxSize);
            }
            else
                candidate = loadRandomFromStorage(uid, maxSize);
        }

        recommendFeedbackLogger.view("goal",uid.toString(), candidate);
        return candidate;
    }


    private List<String> loadRandomFromCache(Long uid, int maxSie)
    {
        String recKey = loader.recKey(uid);
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
        List<String> rec = loader.getCandidatesFromStorage(uid);

        //List<String> filtratedRec = recUser;

        //过滤推荐任务
        List<String> filtratedRec = loader.filter(rec,uid);

        int len = filtratedRec.size();

        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return filtratedRec.subList(0, len);

        int offset = random.nextInt(len - maxSie + 1);
        return filtratedRec.subList(offset, offset+maxSie);
    }

}
