package recommend.service.recommender.v2.impl;


import com.google.common.base.Optional;
import com.zhiyun168.service.api.recommend.v2.IHealthTipRecommender;
import org.springframework.stereotype.Service;
import recommend.model.RecommendProtos.HealthTip;
import recommend.utils.ProtocolBuffersUtil;

import java.util.*;

/**
 * Created by ouduobiao on 2017/1/5.
 */
@Service
public class HealthTipRecommender implements IHealthTipRecommender{

    @Override
    public Map getTip(String uid) {
        HealthTip healthTip = HealthTip.newBuilder()
                .setId(1)
                .setTip("提示")
                .setUrl("feel://user/1")
                .build();

        String str = ProtocolBuffersUtil.toString(healthTip);
        Optional<HealthTip> tip = ProtocolBuffersUtil.toMessage(str, HealthTip.PARSER);

        return ProtocolBuffersUtil.toMap(tip.get());
    }


}


