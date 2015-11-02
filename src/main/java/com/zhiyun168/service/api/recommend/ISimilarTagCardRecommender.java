package com.zhiyun168.service.api.recommend;

import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface ISimilarTagCardRecommender {
    List<String> getCandidates(Long id, int page, int pageSize);

    List<String> getCandidates(List<Long> ids, int page, int pageSize);

    Map<String, String> getCandidatesWithDetail(List<Long> ids, int page, int pageSize);

}
