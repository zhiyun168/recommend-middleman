package recommend.service.health;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.zhiyun168.service.api.recommend.IHealthNoticeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by canoe on 12/29/15.
 */
@Service
class HealthNoticeService implements IHealthNoticeService {

    private Logger log = Logger.getLogger(HealthNoticeService.class);
    private int typeSize = 3;
    private ArrayList<String> esIndexList = Lists.newArrayList("recommend", "recommend", "report");
    private ArrayList<String> esTypeList = Lists.newArrayList("sports", "userstepplan", "body");

    @Autowired
    private EsToMemCacheService esToMemCacheService;

    public Map getNoticeById(Long id, String timeZone) {

        try {
            int index = new Random().nextInt(typeSize);
            String esIndex = esIndexList.get(index);
            String esType = esTypeList.get(index);
            Map tip = new HashMap();
            String content = esToMemCacheService.call(esIndex, esType, id.toString());
            tip.put("content", content);
            Calendar cal  = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone(timeZone);
            cal.setTimeZone(tz);
            tip.put("end_time", System.currentTimeMillis() + 30 * 60 * 10000);
            return tip;
        } catch (Exception e) {
            log.error("notice getting error", e);
            return null;
        }
    }
}
