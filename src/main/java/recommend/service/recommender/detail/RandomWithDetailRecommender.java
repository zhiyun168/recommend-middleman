package recommend.service.recommender.detail;

import com.zhiyun168.model.recommend.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import recommend.service.loader.detail.WithReasonLoader;
import recommend.service.logger.RecommendFeedbackLogger;

import java.util.*;

/**
 * Created by ouduobiao on 15/10/30.
 */

public abstract class RandomWithDetailRecommender implements ApplicationContextAware {
    private static Logger log = LoggerFactory.getLogger(JoinedGoalCardWithDetailRecommender.class);

    private WithReasonLoader withReasonLoader;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RecommendFeedbackLogger recommendFeedbackLogger;

    private Random random = new Random();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        this.recommendFeedbackLogger = applicationContext.getBean(RecommendFeedbackLogger.class);
        this.withReasonLoader = applicationContext.getBean(getLoaderName(), WithReasonLoader.class);
    }

    public abstract  String getLoaderName();

    public Map<String,String> getCandidates_(Long id, int maxSize) {

        Map<String,String> candidate ;

        if(withReasonLoader.hasLoadToCache(id))
        {
            candidate = loadRandomFromCache(id, maxSize);
        }
        else
        {
            boolean hasLoad = withReasonLoader.loadToCache(id);
            if(hasLoad)
            {
                //cache里读
                candidate = loadRandomFromCache(id, maxSize);
            }
            else
                candidate = loadRandomFromStorage(id, maxSize);
        }

        recommendFeedbackLogger.view(withReasonLoader, id.toString(), candidate.keySet());
        return candidate;

    }

    private Map<String,String> loadRandomFromCache(Long id, int maxSie)
    {
        String recKey = withReasonLoader.recKey(id);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recKey);
        int len = ops.size().intValue();
        if(len == 0)
            return Collections.EMPTY_MAP;

        List<String> items;
        if(len <= maxSie)
            items = ops.range(0, -1);
        else
        {
            int offset = random.nextInt(len - maxSie + 1);
            items = ops.range(offset, offset + maxSie - 1);
        }

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

    private Map<String,String> loadRandomFromStorage(Long id, int maxSie)
    {
        Candidate candidate = withReasonLoader.getCandidatesFromStorage(id);

        //List<String> filtratedRec = recUser;

        //过滤推荐任务
        Candidate filtratedCandidate = withReasonLoader.filter(candidate,id);

        List<String> items = filtratedCandidate.getItems();

        int len = items.size();

        if(len == 0)
            return Collections.EMPTY_MAP;

        List<String> recItems;
        if(len <= maxSie)
        {
            recItems = items.subList(0, len);
        }
        else
        {
            int offset = random.nextInt(len - maxSie + 1);
            recItems = items.subList(offset, offset+maxSie);
        }

        int itemSize = recItems.size();
        if(itemSize == 0)
            return Collections.EMPTY_MAP;
        else
        {
            LinkedHashMap<String, String> item_reason = new LinkedHashMap(itemSize);
            Map<String, String> all_reason = candidate.getItemReason();
            for(int i=0;i < itemSize;++i)
            {
                String itemId = recItems.get(i);
                item_reason.put(itemId, all_reason.get(id));
            }
            return item_reason;
        }

    }
}
