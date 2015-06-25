package recommend.service;

import com.google.common.base.Preconditions;
import com.zhiyun168.service.api.IUserService;
import org.redisson.Redisson;
import org.redisson.core.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommend.service.api.IUserRecommender;
import recommend.utils.CacheKeyHelper;

import java.util.*;

/**
 * Created by ouduobiao on 15/6/25.
 */
@Service
public class UserRecommender implements IUserRecommender{

    private static Logger log = LoggerFactory.getLogger(UserRecommender.class);

    @Autowired
    private Loader loader;
    @Autowired
    private Redisson redisson;
    @Autowired
    private IUserService userService;

    private static int MAX_CARD_COUNT = 3;
    Random random = new Random();


    @Override
    public List<String> getCandidates(Long uid, int page, int pageSize) {
        if(loader.hasLoadToCache(uid))
        {
            return loadFromCache(uid, page, pageSize);
        }
        else {
            boolean hasLoad = loader.loadToCache(uid);
            if(hasLoad)
            {
                //cache里读
                return loadFromCache(uid, page, pageSize);
            }
        }
        return loadFromStorage(uid, page, pageSize);
    }

    @Override
    public List<String> getRandomCandidates(Long uid, int maxSize) {
        if(loader.hasLoadToCache(uid))
        {
            return loadRandomFromCache(uid, maxSize);
        }
        else
        {
            boolean hasLoad = loader.loadToCache(uid);
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
        RList<String> recUser = redisson.getList(recUserKey);
        return loadRandomFromList(recUser, maxSie);
    }

    private List<String> loadRandomFromStorage(Long uid, int maxSie)
    {
        List<String> recUser = loader.getRecUserFromStorage(uid);

        //List<String> filtratedRec = recUser;

        //过滤推荐用户
        List<String> filtratedRec = new ArrayList<>();
        for(String recUid: recUser)
        {
            if(userService.getUserCardsCount(Long.parseLong(recUid)) >= MAX_CARD_COUNT)
                filtratedRec.add(recUid);
        }

        return loadRandomFromList(filtratedRec, maxSie);
    }

    private List<String> loadRandomFromList(List<String> list, int maxSie)
    {
        int len = list.size();
        if(len == 0)
            return Collections.EMPTY_LIST;

        //
        if(len <= maxSie)
            return list.subList(0, len);

        Set<Integer> indexes = new LinkedHashSet<>(maxSie);
        while(indexes.size() < maxSie){
            int index = Math.abs(random.nextInt(len));
            indexes.add(index);
        }

        List<String> res = new ArrayList<>(maxSie);
        for(Integer idx : indexes)
        {
            res.add(list.get(idx));
        }
        return res;
    }



    private List<String> loadFromCache(Long uid, int page, int pageSize)
    {
        String recUserKey = CacheKeyHelper.recUserKey(uid);
        RList<String> recUser = redisson.getList(recUserKey);
        return loadFromList(recUser, page, pageSize);
    }

    private List<String> loadFromStorage(Long uid, int page, int pageSize)
    {
        List<String> recUser = loader.getRecUserFromStorage(uid);

        //过滤推荐用户
        List<String> filtratedRec = new ArrayList<>();
        for(String recUid: recUser)
        {
            if(userService.getUserCardsCount(Long.parseLong(recUid)) >= MAX_CARD_COUNT)
                filtratedRec.add(recUid);
        }

        return loadFromList(filtratedRec, page, pageSize);
    }

    private List<String> loadFromList(List<String> list, int page, int pageSize)
    {
        Preconditions.checkArgument(page >0, "page以1为开始");
        int len = list.size();

        int start = (page-1)*pageSize;
        if(start >= len)
            return Collections.EMPTY_LIST;

        int end = start+pageSize;
        if(end > len)
            end = len;

        return list.subList(start, end);
    }

}
