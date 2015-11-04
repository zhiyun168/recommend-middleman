package recommend.service.recommender.detail;

import com.zhiyun168.model.recommend.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import recommend.service.logger.RecommendFeedbackLogger;
import recommend.service.loader.detail.WithReasonLoader;


import java.util.*;

/**
 * Created by ouduobiao on 15/10/30.
 */

public abstract class PagingWithDetailRecommender implements ApplicationContextAware {
    private static Logger log = LoggerFactory.getLogger(JoinedGoalCardWithDetailRecommender.class);

    private WithReasonLoader withReasonLoader;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RecommendFeedbackLogger recommendFeedbackLogger;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        this.recommendFeedbackLogger = applicationContext.getBean(RecommendFeedbackLogger.class);
        this.withReasonLoader = applicationContext.getBean(getLoaderName(), WithReasonLoader.class);
    }

    public abstract  String getLoaderName();

    public Map<String,String> getCandidates_(Long id, int page, int pageSize) {

        if(page < 1)
        {
            log.warn("page should >= 1, but now is :{}, change it to 1", page);
            page = 1;
        }

        Map<String,String> candidate ;

        if(withReasonLoader.hasLoadToCache(id))
        {
            candidate = loadFromCache(id, page, pageSize);
        }
        else
        {
            boolean hasLoad = withReasonLoader.loadToCache(id);
            if(hasLoad)
            {
                //cache里读
                candidate =loadFromCache(id, page, pageSize);
            }
            else
                candidate = loadFromStorage(id, page, pageSize);
        }

        recommendFeedbackLogger.view(withReasonLoader, id.toString(), candidate.keySet());
        return candidate;

    }

    public Map<String,String> loadFromCache(Long id, int page, int pageSize)
    {
        String recKey = withReasonLoader.recKey(id);
        int start = (page - 1)*pageSize;
        int end = start + pageSize - 1;

        List<String> items = stringRedisTemplate.opsForList().range(recKey, start, end);
        int itemSize = items.size();
        if(itemSize == 0)
            return Collections.EMPTY_MAP;

        else
        {
            String reasonKey = withReasonLoader.recReasonKey(id);
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            List<String> reason = hashOperations.multiGet(reasonKey,items);
            LinkedHashMap<String, String> item_reason = new LinkedHashMap(itemSize);
            for(int i=0;i < itemSize;++i)
            {
                item_reason.put(items.get(i), reason.get(i));
            }
            return item_reason;
        }
    }

    public Map<String,String> loadFromStorage(Long uid, int page, int pageSize)
    {
        Candidate rec = withReasonLoader.getCandidatesFromStorage(uid);

        //过滤推荐用户
        Candidate filtratedRec = withReasonLoader.filter(rec, uid);

        return loadFromList(filtratedRec, page, pageSize);
    }

    private Map<String,String> loadFromList(Candidate candidate, int page, int pageSize)
    {

        List<String> list = candidate.getItems();

        int len = list.size();

        int start = (page-1)*pageSize;
        if(start >= len)
            return Collections.EMPTY_MAP;

        int end = start+pageSize;
        if(end > len)
            end = len;

        List<String> sub =  list.subList(start, end);
        int itemSize = sub.size();
        if(itemSize == 0)
            return Collections.EMPTY_MAP;

        else
        {
            LinkedHashMap<String, String> item_reason = new LinkedHashMap(itemSize);
            Map<String, String> all_reason = candidate.getItemReason();
            for(int i=0;i < itemSize;++i)
            {
                String id = sub.get(i);
                item_reason.put(id, all_reason.get(id));
            }
            return item_reason;
        }

    }
}
