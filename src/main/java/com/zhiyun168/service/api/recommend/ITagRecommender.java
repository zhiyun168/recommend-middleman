package com.zhiyun168.service.api.recommend;

import java.util.List;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface ITagRecommender {
    public List<String> getCandidates(Long id, int page, int pageSize);
}
