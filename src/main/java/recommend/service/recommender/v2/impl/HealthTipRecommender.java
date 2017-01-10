package recommend.service.recommender.v2.impl;


import com.google.common.base.Optional;
import com.zhiyun168.service.api.recommend.v2.IHealthTipRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import recommend.model.RecommendProtos.HealthTip;
import recommend.service.recommender.v2.PagingRecommender;
import recommend.utils.ProtocolBuffersUtil;

import java.util.*;

/**
 * 首页可点击文本提示条
 * Created by ouduobiao on 2017/1/5.
 */
@Service("healthTipRecommender")
public class HealthTipRecommender extends PagingRecommender implements IHealthTipRecommender{
    private static Logger log = LoggerFactory.getLogger(HealthTipRecommender.class);

    @Override
    public Map getTip(String uid) {
        List<String> strL = getCandidates_(uid, 1, 1);
        if(CollectionUtils.isEmpty(strL))
            return Collections.EMPTY_MAP;
        else
        {
            Optional<HealthTip> healthTip = ProtocolBuffersUtil.toMessage(strL.get(0), HealthTip.PARSER);
            if(healthTip.isPresent())
                return ProtocolBuffersUtil.toMap(healthTip.get());
            else
                return Collections.EMPTY_MAP;
        }
    }

    @Override
    public String getLoaderName() {
        return "healthTipLoader";
    }
}


