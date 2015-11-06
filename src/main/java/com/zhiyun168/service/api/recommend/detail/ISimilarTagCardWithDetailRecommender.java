package com.zhiyun168.service.api.recommend.detail;

import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface ISimilarTagCardWithDetailRecommender {
    Map<String, String> getCandidates(Long id, int page, int pageSize);
    Map<String, String> getCandidatesWithDetail(List<Long> ids, int page, int pageSize);
}
