package recommend.service.recommender;

import com.zhiyun168.service.api.recommend.INewUserGenderContextRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.service.RecommendFeedbackLogger;
import recommend.service.loader.Loader;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class NewUserGenderContextRecommender implements INewUserGenderContextRecommender {

    private static Logger log = LoggerFactory.getLogger(NewUserGenderContextRecommender.class);

    @Autowired
    @Qualifier("newUserGenderContextLoader")
    private Loader loader;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RecommendFeedbackLogger recommendFeedbackLogger;

    Random random = new Random();

    @Override
    public String getCandidates(Long sex) {
        List<String> candidates = getCandidates(sex, 1);
        if(candidates.isEmpty())
            return null;
        else
            return candidates.get(0);
    }

    /**
     * 获取推荐
     * @param sex：1-男，-1-女
     * @return
     */
    @Override
    public List<String> getCandidates(Long sex, int maxSize)
    {
        List<String> candidate ;

        if(loader.hasLoadToCache(sex))
        {
            candidate = loadRandomFromCache(sex, maxSize);
        }
        else
        {
            boolean hasLoad = loader.loadToCache(sex);
            if(hasLoad)
            {
                //cache里读
                candidate = loadRandomFromCache(sex, maxSize);
            }
            else
                candidate = loadRandomFromStorage(sex, maxSize);
        }

        recommendFeedbackLogger.view("newUserGenderContext",sex.toString(), candidate);
        return candidate;
    }


    private List<String> loadRandomFromCache(Long sex, int maxSie)
    {
        String recKey = loader.recKey(sex);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recKey);
        int len = ops.size().intValue();
        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return ops.range(0, -1);

        int offset = random.nextInt(len-maxSie+1);

        return ops.range(offset, offset + maxSie-1 );
    }

    private List<String> loadRandomFromStorage(Long sex, int maxSie)
    {
        List<String> rec = loader.getCandidatesFromStorage(sex);

        //List<String> filtratedRec = recUser;

        //过滤推荐
        List<String> filtratedRec = loader.filter(rec,sex);

        int len = filtratedRec.size();

        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return filtratedRec.subList(0, len);

        int offset = random.nextInt(len - maxSie + 1);
        return filtratedRec.subList(offset, offset+maxSie);
    }

}
