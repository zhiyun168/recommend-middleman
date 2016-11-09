package recommend.service.recommender.v2.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.zhiyun168.service.api.recommend.v2.ISearchGoalRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import recommend.service.recommender.v2.PagingRecommender;

import java.util.*;


/**
 * Created by ouduobiao on 15/7/13.
 */
@Service("searchGoalRecommender")
public class SearchGoalRecommender extends PagingRecommender implements ISearchGoalRecommender {

    private static Logger log = LoggerFactory.getLogger(SearchGoalRecommender.class);



    @Override
    public List<String> getCandidates(String uid, int page, int pageSize) {
        List<String> res =  getCandidates_(uid, page, pageSize);
        if(CollectionUtils.isEmpty(res))
        {
            res = getCandidates_("new_user", page, pageSize);
        }
        return res;
    }

    @Override
    public String getLoaderName() {
        return "searchRecGoalLoader";
    }


}
