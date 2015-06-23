package recommend.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recommend.utils.CacheKeyHelper;
import recommend.utils.RedisTemplatePlus;
import recommend.utils.StringHelper;

import java.util.Collections;
import java.util.List;



/**
 * Created by ouduobiao on 15/6/23.
 */
@Service
public class Loader {
    private static Logger log = LoggerFactory.getLogger(Loader.class);

    @Autowired
    private SearchClientService searchClientService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static String INDEX = "spark";
    private static String Type = "recommendBasedOnCommonUser";


    /**
     *
     * @param uid
     */
    public boolean loadToCache(Long uid)
    {
        final String lockKey = CacheKeyHelper.recLoadLockKey(uid);
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate, lockKey,
                "", "NX", "EX", 60);
        boolean ok = false;
        if(isLock)
        {
            //获取加载锁，从es加载进redis
            try {
                List<String> rec = getRecUserFromStorage(uid);
                String tmpRecUserKey = CacheKeyHelper.recTmpUserKey(uid);
                String recUserKey = CacheKeyHelper.recUserKey(uid);
                stringRedisTemplate.opsForList().rightPushAll(tmpRecUserKey,rec);
                stringRedisTemplate.delete(recUserKey);
                stringRedisTemplate.rename(tmpRecUserKey, recUserKey);
                ok = true;
            }
            catch (Exception e)
            {
                String msg=StringHelper.DotJoiner.join("加载用户的推荐用户失败", uid);
                log.error(msg, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }

        return ok;
    }


    /**
     *从存储推荐信息的源获取所有推荐用户
     * @param uid
     * @return
     */
    public List<String> getRecUserFromStorage(Long uid)
    {
        Client client = searchClientService.getSearchClient();
        QueryBuilder userQuery = QueryBuilders.termQuery("user", uid.toString());
        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(Type).setQuery(userQuery)
                .setFrom(0)
                .setSize(1)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();
        if(hitList.length > 0)
            return (List<String>)hitList[0].getSource().get("candidates");
        else
            return Collections.EMPTY_LIST;
    }

    public static void main(String[] args) {

    }

}
