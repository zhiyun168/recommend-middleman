package com.zhiyun168.service.api.recommend.detail;

import java.util.Map;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface ICardWithDetailRecommender {
    Map<String, String> getCandidates(Long uid, int page, int pageSize);
}
