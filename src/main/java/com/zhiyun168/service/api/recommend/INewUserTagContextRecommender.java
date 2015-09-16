package com.zhiyun168.service.api.recommend;

import java.util.List;

/**
 * Created by ouduobiao on 15/7/13.
 */
public interface INewUserTagContextRecommender {

    List<String> getCandidates(Long id, int maxSize);

    String getCandidates(Long id);

}
