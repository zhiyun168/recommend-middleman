package com.zhiyun168.service.api.recommend;

import java.util.List;

/**
 * Created by ouduobiao on 15/7/13.
 */
public interface IGoalRecommender {

    public List<String> getCandidates(Long uid, int maxSize);

}
