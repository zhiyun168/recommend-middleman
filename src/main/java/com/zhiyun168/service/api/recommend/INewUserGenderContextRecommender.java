package com.zhiyun168.service.api.recommend;

import java.util.List;

/**
 * Created by ouduobiao on 15/7/13.
 */
public interface INewUserGenderContextRecommender {
    List<String> getCandidates(Long sex, int maxSize);
}
