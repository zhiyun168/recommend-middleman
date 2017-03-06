package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IBannerByUserRecommender;
import org.springframework.stereotype.Service;
import recommend.service.recommender.v2.PagingRecommender;

import java.util.List;

/**
 * Created by ouduobiao on 2017/3/3.
 */
@Service
public class BannerByUserRecommender extends PagingRecommender implements IBannerByUserRecommender {
    @Override
    public List<String> getBanner(String uid, int maxSize) {
        return getCandidates_(uid, 1, maxSize);
    }


    @Override
    public String getLoaderName() {
        return "bannerByUserLoader";
    }
}
