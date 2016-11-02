package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IBannerGoalRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.service.recommender.v2.PagingRecommender;

import java.util.List;


/**
 * Created by ouduobiao on 15/7/13.
 */
@Service("bannerGoalRecommender")
public class BannerGoalRecommender extends PagingRecommender implements IBannerGoalRecommender {
    private static Logger log = LoggerFactory.getLogger(BannerGoalRecommender.class);

    @Override
    public List<String> getCandidates(String id, int page, int pageSize) {
        return getCandidates_(id, page, pageSize);
    }

    @Override
    public String getLoaderName() {
        return "bannerGoalLoader";
    }


}
