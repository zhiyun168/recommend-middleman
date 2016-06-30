package com.zhiyun168.service.api.recommend.v2;

import java.util.List;

/**
 * Created by ouduobiao on 15/7/13.
 */
public interface IGoalRecommender {

    List<String> getCandidates(String uid, int page, int pageSize);
}
