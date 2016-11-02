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

    private static String defaultCandidatesStr = "51203:3289.000000 49284:693.010000 51655:0.000000 46858:122500.460000 51083:3289.000000 51692:748.000000 51697:207.700000 51542:457.800000 51159:0.000000 51096:1293.020000 51357:12283.010000";

    private List<String> defaultCandidates;

    public SearchGoalRecommender() {
        try {
            defaultCandidates = new ArrayList<>();
            Splitter scoreSplitter = Splitter.on(":").omitEmptyStrings().trimResults();
            for(String  itemAndScore: Splitter.on(" ").omitEmptyStrings().trimResults().split(defaultCandidatesStr) )
            {
                List<String> r = scoreSplitter.splitToList(itemAndScore);
                defaultCandidates.add(r.get(0));
            }
        }
        catch (Exception e)
        {
            log.error("初始化默认推荐列表失败");
        }
    }

    @Override
    public List<String> getCandidates(String uid, int page, int pageSize) {
        List<String> res =  getCandidates_(uid, page, pageSize);
        if(CollectionUtils.isEmpty(res))
        {
            List<List<String>> par = Lists.partition(defaultCandidates, pageSize);
            if(page <= par.size())
            {
                res = par.get(page-1);
            }
        }
        return res;
    }

    @Override
    public String getLoaderName() {
        return "searchRecGoalLoader";
    }


}
