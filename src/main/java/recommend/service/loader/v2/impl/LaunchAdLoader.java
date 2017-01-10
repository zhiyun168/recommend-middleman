package recommend.service.loader.v2.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.mapper.CommonRecMapper;
import recommend.model.HealthTipData;
import recommend.model.RecItem;
import recommend.model.RecommendProtos;
import recommend.service.loader.v2.Loader;
import recommend.utils.ProtocolBuffersUtil;

import java.util.Collections;
import java.util.List;

/**
 * 启动后的弹出框个性化
 * Created by ouduobiao on 16/11/2.
 */
@Component("launchAdLoader")
public class LaunchAdLoader extends Loader {
    private static Logger log = LoggerFactory.getLogger(LaunchAdLoader.class);

    @Autowired
    private CommonRecMapper commonRecMapper;


    @Override
    public List<RecItem> getCandidatesFromStorage(String id) {
        try {
            Long uid = Long.parseLong(id);
            String candidate = commonRecMapper.getLaunchAdCandidate(uid);
            if(Strings.isNullOrEmpty(candidate))
                return Collections.EMPTY_LIST;

            RecItem item = new RecItem(candidate, 0.);
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
        return "home_page_popup";
    }
}
