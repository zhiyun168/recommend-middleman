package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IBannerByUserRecommender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ouduobiao on 2017/3/3.
 */
@Service
public class BannerByUserRecommender implements IBannerByUserRecommender {
    @Override
    public List<String> getBanner(String uid, int maxSize) {
        return new ArrayList<>();
    }
}
