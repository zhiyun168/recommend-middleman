package recommend.service.recommender.v2.impl;

import com.zhiyun168.service.api.recommend.v2.IUserTagRecommender;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by ouduobiao on 2017/2/27.
 */
@Service
public class UserTagRecommender implements IUserTagRecommender {

    @Override
    public List<UserTag> getUserTag(String uid, int maxSize) {
        UserTag userTag = new UserTag();
        userTag.setTag("test");
        userTag.setScore(0.);
        return Collections.singletonList(userTag);
    }
}
