package recommend.service.health;

import com.zhiyun168.service.api.IEsToMemcacheService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import recommend.service.MemcachedService;
import recommend.service.SearchClientService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Map<String, Object> call(String index, String type, String id)
    {
        Map ret = new HashMap();
        try {
            String key = index + "_" + type + "_" + id;
            Object redisEsFlagInfo = stringRedisTemplate.opsForHash().get("feel_health_" + index + "_" + type, id);
            if (redisEsFlagInfo == null) {
                stringRedisTemplate.opsForHash().put("feel_health_" + index + "_" + type, id, String.valueOf(System
                        .currentTimeMillis()));

                Client client = searchClientService.getSearchClient();
                GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
                Map<String, Object> value =
                        response.getSource() == null ? Collections.EMPTY_MAP : response.getSource();
                memcachedService.set(key, value, 60 * 60 * 24);
                ret.put("value", value);
                return ret;
            } else {
                Object memeachedInfo = memcachedService.get(key);
                if (memeachedInfo == null) {
                    Client client = searchClientService.getSearchClient();
                    GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
                    Map<String, Object> value =
                            response.getSource() == null ? Collections.EMPTY_MAP : response.getSource();
                    memcachedService.set(key, value, 60 * 60 * 24);
                    ret.put("value", value);
                    return ret;
                } else {
                    ret.put("value", memeachedInfo);
                    return ret;
                }
            }
        }
        catch (Exception e) {
            log.error("error in est", e);
            ret.put("value", Collections.EMPTY_MAP);
            return ret;
        }
    }
}
