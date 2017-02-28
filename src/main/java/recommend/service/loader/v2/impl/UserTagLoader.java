package recommend.service.loader.v2.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.mapper.CommonRecMapper;
import recommend.model.RecItem;
import recommend.service.loader.v2.Loader;

import java.util.Collections;
import java.util.List;

/**
 * 用户标签
 * Created by ouduobiao on 16/11/2.
 */
@Component("userTagLoader")
public class UserTagLoader extends Loader {
    private static Logger log = LoggerFactory.getLogger(UserTagLoader.class);

    @Autowired
    private CommonRecMapper commonRecMapper;


    @Override
    public List<RecItem> getCandidatesFromStorage(String id) {
        try {

            List<RecItem> candidate = commonRecMapper.getUserTag(id);

            return candidate;
        }
        catch (Exception e)
        {
            log.error("加载candidate失败", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String recName() {
        return "user_tag";
    }
}
