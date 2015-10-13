package com.zhiyun168.service.api;

import java.util.Map;


/**
 * Created by lipf on 2014/7/14.
 */
public interface ICardService {
    /**
     * 根据id获取card信息
     * @param id
     */
    Map findCardById(Long id);

}
