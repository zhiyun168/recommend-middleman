package recommend.service.loader.v2.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.mapper.RecGoalMapper;
import recommend.model.RecItem;
import recommend.service.loader.v2.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ouduobiao on 16/6/28.
 */
@Component("recGoalLoaderV2")
public class RecGoalLoader extends Loader{

    private static Logger log = LoggerFactory.getLogger(RecGoalLoader.class);

    @Autowired
    private RecGoalMapper recGoalMapper;

    @Override
    public List<RecItem> getCandidatesFromStorage(String id) {
        String candidateStr=null;
        try {
            candidateStr = recGoalMapper.getCandidate(id);
            if(Strings.isNullOrEmpty(candidateStr))
                return Collections.EMPTY_LIST;
            else
            {
                Splitter scoreSplitter = Splitter.on(":").omitEmptyStrings().trimResults();
                List<RecItem> recItems = new ArrayList<>();
                for(String  itemAndScore: Splitter.on(",").omitEmptyStrings().trimResults().split(candidateStr) )
                {
                    try {
                        List<String> r = scoreSplitter.splitToList(itemAndScore);
                        recItems.add(new RecItem(r.get(0), Double.parseDouble(r.get(1))));
                    }
                    catch (Exception e)
                    {
                        log.error("解析candidate item出错:"+itemAndScore, e);
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
        return "GoalRecV2";
    }

}
