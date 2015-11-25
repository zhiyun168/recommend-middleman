package recommend.service.loader.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class GenderGoalWithDetailLoader extends WithReasonLoader {

    protected static Logger log = LoggerFactory.getLogger(GenderGoalWithDetailLoader.class);

    public static String REC_NAME = "gender_goal_with_detail";

    @Override
    public String getEsIndexName() {
        return "recommendation";
    }

    @Override
    public String getEsType() {
        return "gender_goal_with_detail";
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
