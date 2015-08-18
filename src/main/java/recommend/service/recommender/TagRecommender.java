package recommend.service.recommender;

import com.zhiyun168.service.api.recommend.ITagRecommender;
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

/**
 * Created by ouduobiao on 15/6/24.
 */
@Service
public class TagRecommender implements ITagRecommender{

    private static Logger log = LoggerFactory.getLogger(TagRecommender.class);


    @Autowired
    @Qualifier("recTagLoader")
    private Loader loader;
    @Autowired
    private RecommendFeedbackLogger recommendFeedbackLogger;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> getCandidates(Long id, int page, int pageSize) {

        if(page < 1)
        {
            log.warn("page should >= 1, but now is :{}, change it to 1", page);
            page = 1;
        }

        List<String> candidate ;

        if(loader.hasLoadToCache(id))
        {
            candidate = loadFromCache(id, page, pageSize);
        }
        else
        {
            boolean hasLoad = loader.loadToCache(id);
            if(hasLoad)
            {
                //cache里读
                candidate =loadFromCache(id, page, pageSize);
            }
            else
                candidate = loadFromStorage(id, page, pageSize);
        }

        recommendFeedbackLogger.view("tag",id.toString(), candidate);
        return candidate;
    }

    public List<String> loadFromCache(Long id, int page, int pageSize)
    {
        String recKey = loader.recKey(id);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recKey);

        int start = (page - 1)*pageSize;
        int end = start + pageSize - 1;

        return ops.range(start, end);
    }

    public List<String> loadFromStorage(Long id, int page, int pageSize)
    {
        List<String> rec = loader.getCandidatesFromStorage(id);

        //过滤推荐
        List<String> filtratedRec = loader.filter(rec, id);

        return loadFromList(filtratedRec, page, pageSize);
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
