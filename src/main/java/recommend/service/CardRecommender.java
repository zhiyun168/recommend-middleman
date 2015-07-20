package recommend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.zhiyun168.service.api.recommend.ICardRecommender;
import recommend.service.loader.Loader;

import java.util.Collections;
import java.util.List;

/**
 * Created by ouduobiao on 15/7/20.
 */
@Service
public class CardRecommender implements ICardRecommender{

    private static Logger log = LoggerFactory.getLogger(CardRecommender.class);

    @Autowired
    @Qualifier("recCardLoader")
    private Loader loader;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> getCandidates(Long uid, int page, int pageSize) {

        if(page < 1)
        {
            log.warn("page should >= 1, but now is :{}, change it to 1", page);
            page = 1;
        }


        if(!loader.hasLoadToCache(uid))
        {
            boolean hasLoad = loader.loadToCache(uid);
            if(hasLoad)
            {
                //cache里读
                return loadFromCache(uid, page, pageSize);
            }
        }

        return loadFromStorage(uid, page, pageSize);
    }

    public List<String> loadFromCache(Long uid, int page, int pageSize)
    {
        String recKey = loader.recKey(uid);
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(recKey);

        int start = (page - 1)*pageSize;
        int end = start + pageSize;

        return ops.range(start, end);
    }

    public List<String> loadFromStorage(Long uid, int page, int pageSize)
    {
        List<String> rec = loader.getCandidatesFromStorage(uid);

        //过滤推荐用户
        List<String> filtratedRec = loader.filter(rec, uid);

        return loadFromList(filtratedRec, page, pageSize);
    }

    private List<String> loadFromList(List<String> list, int page, int pageSize)
    {

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
