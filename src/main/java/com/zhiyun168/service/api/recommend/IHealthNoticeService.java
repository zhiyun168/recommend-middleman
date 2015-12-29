package com.zhiyun168.service.api.recommend;

import java.util.Map;

/**
 * Created by canoe on 12/29/15.
 */
public interface IHealthNoticeService {

    Map getNoticeById(Long id, String timeZone);
}
