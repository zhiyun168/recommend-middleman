package com.zhiyun168.service.api.recommend.v2;

import java.util.List;

/**
 * Created by ouduobiao on 15/7/13.
 */
public interface IBannerGoalRecommender {

    List<String> getCandidates(String id, int page, int pageSize);
    List<String> getCandidates(String id, String uid, int page, int pageSize);

}
