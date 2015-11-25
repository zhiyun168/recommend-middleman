package com.zhiyun168.service.api.recommend.detail;

import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/7/13.
 */
public interface IGoalCommonUserGoalWithDetailRecommender {

    Map<String, String> getCandidates(Long uid, int maxSize);

}
