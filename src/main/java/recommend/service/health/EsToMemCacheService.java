package recommend.service.health;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyun168.service.api.IEsToMemcacheService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import recommend.service.MemcachedService;
import recommend.service.SearchClientService;

/**
 * Created by canoe on 12/29/15.
 */
@Service
public class EsToMemCacheService implements IEsToMemcacheService {
    private static Logger log = LoggerFactory.getLogger(EsToMemCacheService.class);

    @Autowired
    private MemcachedService memcachedService;
    @Autowired
    private SearchClientService searchClientService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String call(String index, String type, String id)
    {
        try {
            String key = index + "_" + type + "_" + id;
            Object redisEsFlagInfo = stringRedisTemplate.opsForHash().get("feel_health_" + index + "_" + type, id);

            if (redisEsFlagInfo == null) {
                stringRedisTemplate.opsForHash().put("feel_health_" + index + "_" + type, id, System.currentTimeMillis());

                Client client = searchClientService.getSearchClient();
                GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
                memcachedService.set(key, response.getSource(), 60 * 60 * 24);
                return response.getSource().toString();
            } else {
                Object memeachedInfo = memcachedService.get(key);
                if (memeachedInfo == null) {
                    Client client = searchClientService.getSearchClient();
                    GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
                    memcachedService.set(key, response.getSource(), 60 * 60 * 24);
                    return response.getSource().toString();
                } else {
                    return memeachedInfo.toString();
                }
            }
        }
        catch (Exception e) {
            log.error("error in est", e);
            return "";
        }
    }
}
