package recommend.service.loader.v2.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.mapper.CommonRecMapper;
import recommend.mapper.RecGoalMapper;
import recommend.model.HealthTipData;
import recommend.model.RecItem;
import recommend.model.RecommendProtos;
import recommend.service.loader.v2.Loader;
import recommend.utils.ProtocolBuffersUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 首页可点击文本提示条
 * Created by ouduobiao on 16/11/2.
 */
@Component("healthTipLoader")
public class HealthTipLoader extends Loader {
    private static Logger log = LoggerFactory.getLogger(HealthTipLoader.class);

    @Autowired
    private CommonRecMapper commonRecMapper;


    @Override
    public List<RecItem> getCandidatesFromStorage(String id) {
        try {
            Long uid = Long.parseLong(id);
            HealthTipData healthTipData = commonRecMapper.getHealthTipCandidate(uid);
            if(healthTipData == null)
                return Collections.EMPTY_LIST;
            RecommendProtos.HealthTip healthTip = RecommendProtos.HealthTip.newBuilder()
                    .setId(healthTipData.getId())
                    .setTip(healthTipData.getTip())
                    .setUrl(healthTipData.getUrl())
                    .build();
            RecItem item = new RecItem(ProtocolBuffersUtil.toString(healthTip), 0.);
            return Collections.singletonList(item);
        }
        catch (Exception e)
        {
            log.error("加载candidate失败", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String recName() {
        return "home_page_textbar";
    }
}
