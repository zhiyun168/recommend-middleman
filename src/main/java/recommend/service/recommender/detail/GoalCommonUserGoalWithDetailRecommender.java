package recommend.service.recommender.detail;

import com.zhiyun168.service.api.recommend.detail.IGoalCommonUserGoalWithDetailRecommender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ouduobiao on 15/10/30.
 */
@Service
public class GoalCommonUserGoalWithDetailRecommender extends RandomWithDetailRecommender implements IGoalCommonUserGoalWithDetailRecommender {
    @Override
    public Map<String, String> getCandidates(Long uid, int maxSize) {
        return getCandidates_(uid, maxSize);
    }

    @Override
    public String getLoaderName() {
        return "goalCommonUserGoalWithDetailLoader";
    }
}
