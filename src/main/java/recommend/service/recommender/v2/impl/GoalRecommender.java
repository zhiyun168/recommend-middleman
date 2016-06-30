package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IGoalRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import recommend.service.recommender.v2.PagingRecommender;


import java.util.*;


/**
 * Created by ouduobiao on 15/7/13.
 */
@Service("goalRecommenderV2")
public class GoalRecommender extends PagingRecommender implements IGoalRecommender {

    private static Logger log = LoggerFactory.getLogger(GoalRecommender.class);

    @Override
    public List<String> getCandidates(String uid, int page, int pageSize) {
        return getCandidates_(uid, page, pageSize);
    }

    @Override
    public List<String> loadFromCache(String id, int page, int pageSize)
    {
        String recKey = loader.recKey(id);
        int start = (page - 1)*pageSize;
        int end = start + pageSize - 1;
        Set<ZSetOperations.TypedTuple<String>>  candidates= stringRedisTemplate.opsForZSet().reverseRangeWithScores(recKey, start, end);
        int size = candidates.size();
        if(size == 0)
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            List<String> items = new ArrayList<>(size);
            Set<ZSetOperations.TypedTuple<String>>  newCandidates = new HashSet(size);
            for(ZSetOperations.TypedTuple<String> candidate : candidates)
            {
                items.add(candidate.getValue());
                newCandidates.add(new DefaultTypedTuple<>(candidate.getValue(), candidate.getScore()-1));
            }
            stringRedisTemplate.opsForZSet().add(recKey, newCandidates);

            return items;
        }
    }

    @Override
    public String getLoaderName() {
        return "recGoalLoaderV2";
    }


}
