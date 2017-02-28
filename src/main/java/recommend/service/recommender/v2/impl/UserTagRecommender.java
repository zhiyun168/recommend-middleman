package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IUserTagRecommender;
import org.springframework.stereotype.Service;
import recommend.service.recommender.v2.PagingRecommender;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ouduobiao on 2017/2/27.
 */
@Service
public class UserTagRecommender extends PagingRecommender implements IUserTagRecommender {


    @Override
    public String getLoaderName() {
        return "userTagLoader";
    }

    @Override
    public List<UserTag> getUserTag(String uid, int maxSize) {

        List<String> candidates = this.getCandidates_(uid, 1, maxSize);
        List<UserTag> userTags = new LinkedList<>();
        for(String candidate: candidates)
        {
            UserTag userTag = new UserTag();
            userTag.setTag(candidate);
            userTag.setScore(1.);
            userTags.add(userTag);
        }
        return userTags;
    }
}
