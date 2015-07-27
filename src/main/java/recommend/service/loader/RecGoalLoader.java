package recommend.service.loader;

import com.google.common.base.Preconditions;
import com.zhiyun168.service.api.goal.IGoalService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class RecGoalLoader extends Loader {

    private static Logger log = LoggerFactory.getLogger(RecGoalLoader.class);

    @Autowired
    private IGoalService goalService;

    private static String ES_Type = "genderGoal";


    @Override
    public String recKey(Long uid) {
        return CacheKeyHelper.recGoalKey(uid);
    }

    @Override
    public String recTmpKey(Long uid) {
        return CacheKeyHelper.recTmpGoalKey(uid);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_GOAL_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long uid) {
        return CacheKeyHelper.recGoalLockKey(uid);
    }

    @Override
    public String getEsType() {
        return ES_Type;
    }

    @Override
    public List<String> filter(List<String> rec, Long uid)
    {
        //return  rec;

        //过滤推荐任务
        List<String> filtratedRec = new ArrayList<>();
        for(String recId: rec)
        {
            Long rec_id = Long.parseLong(recId);
            Boolean isJoin = goalService.isJoinGoal(rec_id, uid);
            if(isJoin!=null && !isJoin)
                filtratedRec.add(recId);
        }
        return  filtratedRec;

    }


}
