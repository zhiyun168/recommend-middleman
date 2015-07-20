package recommend.service;

import com.zhiyun168.service.api.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.service.api.IUserRecommender;
import recommend.service.loader.RecUserLoader;
import recommend.utils.CacheKeyHelper;

import java.util.*;

/**
 * Created by ouduobiao on 15/6/25.
 */
@Service
public class UserRecommender2 implements IUserRecommender{

    private static Logger log = LoggerFactory.getLogger(UserRecommender2.class);

    @Autowired
    private RecUserLoader recUserLoader;
    @Autowired
    private IUserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static int MAX_CARD_COUNT = 3;
    Random random = new Random();




    @Override
    public List<String> getRandomCandidates(Long uid, int maxSize) {
        if(recUserLoader.hasLoadToCache(uid))
        {
            return loadRandomFromCache(uid, maxSize);
        }
        else
        {
            boolean hasLoad = recUserLoader.loadToCache(uid);
            if(hasLoad)
            {
                //cache里读
                return loadRandomFromCache(uid, maxSize);
            }
        }
        return loadRandomFromStorage(uid, maxSize);
    }

    private List<String> loadRandomFromCache(Long uid, int maxSie)
    {
        String recUserKey = CacheKeyHelper.recUserKey(uid);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recUserKey);
        int len = ops.size().intValue();
        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return ops.range(0, -1);

        int offset = random.nextInt(len-maxSie+1);

        return ops.range(offset, offset + maxSie-1 );
    }

    private List<String> loadRandomFromStorage(Long uid, int maxSie)
    {
        List<String> recUser = recUserLoader.getRecUserFromStorage(uid);

        //List<String> filtratedRec = recUser;

        //过滤推荐用户
        List<String> filtratedRec = new ArrayList<>();
        for(String recUid: recUser)
        {
            Long rec_id = Long.parseLong(recUid);
            Long cardCount = userService.getUserCardsCount(rec_id);
            if(cardCount!=null && cardCount >= MAX_CARD_COUNT)
                filtratedRec.add(recUid);
        }

        int len = filtratedRec.size();

        if(len == 0)
            return Collections.EMPTY_LIST;

        if(len <= maxSie)
            return filtratedRec.subList(0, len);

        int offset = random.nextInt(len - maxSie + 1);
        return filtratedRec.subList(offset, offset+maxSie);
    }


    @Override
    public List<String> getCandidates(Long uid, int page, int pageSize) {
        return Collections.EMPTY_LIST;
    }



}
