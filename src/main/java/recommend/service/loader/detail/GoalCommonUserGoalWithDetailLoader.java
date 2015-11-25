package recommend.service.loader.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class GoalCommonUserGoalWithDetailLoader extends WithReasonLoader {

    protected static Logger log = LoggerFactory.getLogger(GoalCommonUserGoalWithDetailLoader.class);

    public static String REC_NAME = "goal_common_user_goal";

    @Override
    public String getEsIndexName() {
        return "recommendation";
    }

    @Override
    public String getEsType() {
        return "goalCommonUserGoal";
    }

    @Override
    public String getEsIdField() {
        return "user";
    }

    @Override
    public String recName() {
        return REC_NAME;
    }

}
