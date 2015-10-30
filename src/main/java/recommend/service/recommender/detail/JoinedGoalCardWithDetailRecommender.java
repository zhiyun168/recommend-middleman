package recommend.service.recommender.detail;

import com.zhiyun168.service.api.recommend.IJoinedGoalCardWithDetailRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ouduobiao on 15/7/20.
 */
@Service
public class JoinedGoalCardWithDetailRecommender extends PagingWithDetailRecommender implements IJoinedGoalCardWithDetailRecommender{

    private static Logger log = LoggerFactory.getLogger(JoinedGoalCardWithDetailRecommender.class);

    @Override
    public Map<String, String> getCandidates(Long id, int page, int pageSize) {
        return getCandidates_(id, page, pageSize);
    }

    @Override
    public String getLoaderName() {
        return "joinedGoalCardWithDetailLoader";
    }
}
