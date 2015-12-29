package recommend.service.health;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyun168.service.api.recommend.IHealthNoticeService;

import java.util.Map;

/**
 * Created by canoe on 12/29/15.
 */
@Service
class HealthNoticeService implements IHealthNoticeService {

    @Override
    public Map getNoticeById(Long id, String timeZone) {
        return null;
    }
}
