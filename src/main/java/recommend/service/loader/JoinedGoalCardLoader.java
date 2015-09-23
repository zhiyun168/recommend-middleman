package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class JoinedGoalCardLoader extends Loader {

    protected static Logger log = LoggerFactory.getLogger(JoinedGoalCardLoader.class);

    private static String ES_Index = "recommendation_joined_goal_card";
    private static String ES_Type = "CARD";
    private static String ES_ID_FIELD = "user";

    @Override
    public String getEsIndexName() {
        return ES_Index;
    }

    @Override
    public String recKey(Long uid) {
        return CacheKeyHelper.recJoinedGoalCardKey(uid);
    }

    @Override
    public String recTmpKey(Long uid) {
        return CacheKeyHelper.recTmpJoinedGoalCardKey(uid);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_JOINED_GOAL_CARD_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long uid) {
        return CacheKeyHelper.recJoinedGoalCardLockKey(uid);
    }

    @Override
    public String getEsType() {
        return ES_Type;
    }

    @Override
    public String getEsIdField() {
        return ES_ID_FIELD;
    }
}
