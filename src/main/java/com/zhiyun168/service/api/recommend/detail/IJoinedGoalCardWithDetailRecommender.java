package com.zhiyun168.service.api.recommend.detail;

import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface IJoinedGoalCardWithDetailRecommender {
    Map<String, String> getCandidates(Long uid, int page, int pageSize);
}
