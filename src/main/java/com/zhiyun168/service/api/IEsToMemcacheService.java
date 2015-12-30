package com.zhiyun168.service.api;

/**
 * Created by canoe on 12/29/15.
 */
public interface IEsToMemcacheService {

        String call(String index, String type, String id);
}

