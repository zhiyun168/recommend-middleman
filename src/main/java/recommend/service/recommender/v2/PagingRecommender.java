package recommend.service.recommender.v2;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import recommend.model.RecItem;
import recommend.service.loader.v2.Loader;

import java.util.*;

/**
 * Created by ouduobiao on 15/10/30.
 */

public abstract class PagingRecommender implements ApplicationContextAware {
    private static Logger log = LoggerFactory.getLogger(PagingRecommender.class);

    private Loader loader;
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        this.loader = applicationContext.getBean(getLoaderName(), Loader.class);
    }

    public abstract String getLoaderName();

    public List<String> getCandidates_(String id, int page, int pageSize) {

        if(page < 1)
        {
            log.warn("page should >= 1, but now is :{}, change it to 1", page);
            page = 1;
        }

        List<String> candidate ;

        if(loader.hasLoadToCache(id))
        {
            candidate = loadFromCache(id, page, pageSize);
        }
        else
        {
            boolean hasLoad = loader.loadToCache(id);
            if(hasLoad)
            {
                //cache里读
                candidate =loadFromCache(id, page, pageSize);
            }
            else
                candidate = loadFromStorage(id, page, pageSize);
        }
        log.info(candidate.toString());

        return candidate;

    }

    public List<String> loadFromCache(String id, int page, int pageSize)
    {
        String recKey = loader.recKey(id);
        int start = (page - 1)*pageSize;
        int end = start + pageSize - 1;
        List<String> items = new ArrayList<>(stringRedisTemplate.opsForZSet().reverseRange(recKey, start, end));
        return items;

    }

    public List<String> loadFromStorage(String uid, int page, int pageSize)
    {
        List<List<RecItem>> par = Lists.partition(loader.getCandidatesFromStorage(uid), pageSize);
        if(page > par.size())
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            List<RecItem> subRecItems = par.get(page-1);
            List<String> items =  new ArrayList<>(subRecItems.size());
            for(RecItem recItem: subRecItems)
            {
                items.add(recItem.getCandidate());
            }
            return items;
        }
    }

}
