package com.zhiyun168.service.api.recommend.v2;

import java.util.List;

/**
 * Created by ouduobiao on 2017/2/27.
 */
public interface IBannerByUserRecommender {

    List<String> getBanner(String uid, int maxSize);

}
