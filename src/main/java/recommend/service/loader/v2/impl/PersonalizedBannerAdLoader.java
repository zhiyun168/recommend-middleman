package recommend.service.loader.v2.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.mapper.CommonRecMapper;
import recommend.mapper.RecGoalMapper;
import recommend.model.RecItem;
import recommend.service.loader.v2.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 首页全局banner位个性化
 * Created by ouduobiao on 16/11/2.
 */
@Component("personalizedBannerAdLoader")
public class PersonalizedBannerAdLoader extends Loader {
    private static Logger log = LoggerFactory.getLogger(PersonalizedBannerAdLoader.class);

    @Autowired
    private RecGoalMapper recGoalMapper;


    @Override
    public List<RecItem> getCandidatesFromStorage(String id) {
        String candidateStr=null;
        try {

            Splitter splitter = Splitter.on(" ").omitEmptyStrings().trimResults();
            Iterator<String> iter = splitter.split(id).iterator();
            Long uid = Long.parseLong(iter.next());
            Long goal_id = Long.parseLong(iter.next());
            candidateStr = recGoalMapper.getPersonalizedBannerCandidate(uid, goal_id);
            if(Strings.isNullOrEmpty(candidateStr))
                return Collections.EMPTY_LIST;
            else
            {
                List<RecItem> recItems = new ArrayList<>();
                double score = 0.;
                for(String  item: splitter.split(candidateStr) )
                {
                    try {

                        recItems.add(new RecItem(item, score));
                        score -= 1.;
                    }
                    catch (Exception e)
                    {
                        log.error("解析candidate item出错:"+item, e);
                    }
                }
                return recItems;
            }
        }
        catch (Exception e)
        {
            log.error("解析candidate:"+candidateStr, e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String recName() {
        return "banner_personalized";
    }
}
