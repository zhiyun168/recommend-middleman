package recommend.service.recommender;

import com.zhiyun168.model.recommend.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.zhiyun168.service.api.recommend.ICardRecommender;
import recommend.service.logger.RecommendFeedbackLogger;
import recommend.service.loader.detail.WithReasonLoader;

import java.util.Collections;
import java.util.List;

/**
 * Created by ouduobiao on 15/7/20.
 */
@Service
public class CardRecommender implements ICardRecommender{

    private static Logger log = LoggerFactory.getLogger(CardRecommender.class);

    @Autowired
    @Qualifier("recCardWithDetailLoader")
    private WithReasonLoader loader;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RecommendFeedbackLogger recommendFeedbackLogger;

    @Override
    public List<String> getCandidates(Long uid, int page, int pageSize) {

        if(page < 1)
        {
            log.warn("page should >= 1, but now is :{}, change it to 1", page);
            page = 1;
        }

        List<String> candidate ;

        if(loader.hasLoadToCache(uid))
        {
            candidate = loadFromCache(uid, page, pageSize);
        }
        else
        {
            boolean hasLoad = loader.loadToCache(uid);
            if(hasLoad)
            {
                //cache里读
                candidate =loadFromCache(uid, page, pageSize);
            }
            else
                candidate = loadFromStorage(uid, page, pageSize);
        }

        recommendFeedbackLogger.view("card",uid.toString(), candidate);
        return candidate;
    }

    public List<String> loadFromCache(Long uid, int page, int pageSize)
    {
        String recKey = loader.recKey(uid);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recKey);

        int start = (page - 1)*pageSize;
        int end = start + pageSize - 1;

        return ops.range(start, end);
    }

    public List<String> loadFromStorage(Long uid, int page, int pageSize)
    {
        Candidate candidate = loader.getCandidatesFromStorage(uid);

        //过滤推荐用户
        Candidate filtratedRec = loader.filter(candidate, uid);

        return loadFromList(filtratedRec.getItems(), page, pageSize);
    }

    private List<String> loadFromList(List<String> list, int page, int pageSize)
    {

        int len = list.size();

        int start = (page-1)*pageSize;
        if(start >= len)
            return Collections.EMPTY_LIST;

        int end = start+pageSize;
        if(end > len)
            end = len;

        return list.subList(start, end);
    }


}
