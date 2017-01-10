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
@Service("personalizedBannerRecommender")
public class PersonalizedBannerRecommender extends PagingRecommender  {
    private static Logger log = LoggerFactory.getLogger(PersonalizedBannerRecommender.class);



    @Override
    public String getLoaderName() {
        return "personalizedBannerAdLoader";
    }


}
