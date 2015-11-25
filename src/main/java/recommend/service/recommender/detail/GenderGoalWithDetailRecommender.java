package recommend.service.recommender.detail;

import com.zhiyun168.service.api.recommend.detail.IGenderGoalWithDetailRecommender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ouduobiao on 15/11/17.
 */
@Service
public class GenderGoalWithDetailRecommender extends RandomWithDetailRecommender implements IGenderGoalWithDetailRecommender {
    @Override
    public Map<String, String> getCandidates(Long uid, int maxSize) {
        return getCandidates_(uid, maxSize);
    }

    @Override
    public String getLoaderName() {
        return "genderGoalWithDetailLoader";
    }


}
