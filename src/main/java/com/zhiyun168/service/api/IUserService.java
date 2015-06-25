package com.zhiyun168.service.api;


import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/7/14.
 */
public interface IUserService {



    /**
     * 用户发布的卡片数
     * @param uid
     * @return
     */
    public Long getUserCardsCount(Long uid);

}
