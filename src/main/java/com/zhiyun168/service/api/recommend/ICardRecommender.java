package com.zhiyun168.service.api.recommend;

import java.util.List;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface ICardRecommender {
    public List<String> getCandidates(Long uid, int page, int pageSize);
}
