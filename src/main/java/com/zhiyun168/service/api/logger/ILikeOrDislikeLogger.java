package com.zhiyun168.service.api.logger;

/**
 * Created by ouduobiao on 15/11/4.
 */
public interface ILikeOrDislikeLogger {

    void dislike(Long uid, Long cardId, String collection);
    void like(Long uid, Long cardId, String collection);

}
