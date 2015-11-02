package recommend.service.recommender.detail;

import com.zhiyun168.service.api.recommend.detail.ISimilarUserCardWithDetailRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * Created by ouduobiao on 15/7/20.
 */
@Service
public class SimilarUserCardWithDetailRecommender extends PagingWithDetailRecommender implements ISimilarUserCardWithDetailRecommender {

    private static Logger log = LoggerFactory.getLogger(SimilarUserCardWithDetailRecommender.class);

    @Override
    public Map<String, String> getCandidates(Long uid, int page, int pageSize) {
        return getCandidates_(uid, page, pageSize);
    }

    @Override
    public String getLoaderName() {
        return "similarUserCardWithDetailLoader";
    }
}
