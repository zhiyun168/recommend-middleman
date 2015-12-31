package com.zhiyun168.service.api;

import recommend.utils.ObjectUtil;

import java.util.Map;

/**
 * Created by canoe on 12/29/15.
 */
public interface IEsToMemcacheService {

        Map<String, Object> call(String index, String type, String id);
}

