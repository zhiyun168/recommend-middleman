package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IFocusUserRecommender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import recommend.service.recommender.v2.PagingRecommender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouduobiao on 2016/11/8.
 */
@Service
public class FocusUserRecommender extends PagingRecommender implements IFocusUserRecommender {
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
        return "focusUserRecLoader";
    }
}
