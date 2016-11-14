package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IFocusUserRecommender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouduobiao on 2016/11/8.
 */
@Service
public class FocusUserRecommender implements IFocusUserRecommender {
    private static Long start = 4000000L;
    @Override
    public List<String> getCandidates(String uid, int page, int pageSize) {
        List<String> uIds = new ArrayList<>();
        Long s = (page-1) * pageSize + start;
        Long end = s + pageSize;
        for(Long i = s; i < end;++i)
        {
            uIds.add(i.toString());
        }
        return uIds;
    }
}
