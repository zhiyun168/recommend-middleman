package recommend.service;

import com.google.code.ssm.Cache;
import com.google.code.ssm.api.format.SerializationType;
import com.google.code.ssm.providers.CacheException;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * memcached service 与cacheManager类似
 * 封装memcache方法
 */
@Service
public class MemcachedService {
    private Logger log = Logger.getLogger(MemcachedService.class);


    //设置默认的缓存序列化方式
    private static final SerializationType serializationType = SerializationType.PROVIDER;
    @Autowired
    @Qualifier("defaultMemcachedClient")
    Cache cache;

    /**
     * 设置缓存永不过期
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        set(key, value, 0);
    }

    /**
     * 设置缓存 时间为s
     * @param key
     * @param value
     * @param exp
     */
    public void set(String key, Object value, int exp){
        try {
            cache.set(key, exp, value, serializationType);
        } catch (TimeoutException e) {
            log.error("set cache timeout:" + key, e);
        } catch (CacheException e) {
            log.error("set cache failed:" + key, e);
        }
    }

    public Object get(String key){
        Object ret = null;
        try {
            ret = cache.get(key, serializationType);
        } catch (TimeoutException e) {
            log.error("get cache timeout:" + key, e);
        } catch (CacheException e) {
            log.error("get cache failed:" + key, e);
        }
        return ret;
    }

    public Map<String,Object> gets(Collection<String> keys){
        Map<String,Object> ret = Collections.EMPTY_MAP;
        try {
            ret = cache.getBulk(keys, serializationType);
        } catch (TimeoutException e) {
            log.error("get cache timeout:" + keys, e);
        } catch (CacheException e) {
            log.error("get cache failed:" + keys, e);
        }
        return ret;
    }

    public void delete(String key){
        try {
            cache.delete(key);
        } catch (TimeoutException e) {
            log.error("delete cache timeout:" + key, e);
        } catch (CacheException e) {
            log.error("delete cache failed:" + key, e);
        }
    }

    public void delete(Collection<String> keys){
        if(keys != null && !keys.isEmpty()){
            try {
                cache.delete(keys);
            } catch (TimeoutException e) {
                log.error("delete cache timeout:" + keys.size(), e);
            } catch (CacheException e) {
                log.error("delete cache failed:" + keys.size(), e);
            }
        }
    }


    public boolean add(final String key, final int exp, final Object value)
    {
        boolean res = false;
        MemcachedClient client = (MemcachedClient)cache.getNativeClient();
        try {
            res = client.add(key,exp,value);
        } catch (TimeoutException e) {
            log.error("add cache timeout:" + key, e);
        } catch (InterruptedException e) {
            log.error("add cache InterruptedException:" + key, e);
        } catch (MemcachedException e) {
            log.error("add cache MemcachedException:" + key, e);
        }
        return res;
    }


    public Object getNativeClient()
    {
        return  cache.getNativeClient();
    }



    /**
     * 递增计数器
     * odb
     * @param
     */
    public void increaseCounter(String key, int exp, DefaultCounter defaultCounter)
    {
        try {
            MemcachedClient cacheClient = (MemcachedClient) cache.getNativeClient();
            Long res = cacheClient.incr(key, 1, -1);
            if (res.equals(-1L)) {
                Long dbRes = defaultCounter.initValue();
                cacheClient.set(key, exp, dbRes.toString());
            }
        } catch (Exception e) {
            log.warn("递增计数器", e);
        }
    }

    /**
     * 递减计数器
     * odb
     * @param
     */
    public void decreaseCounter(String key, int exp, DefaultCounter defaultCounter)
    {
        try {
            MemcachedClient cacheClient = (MemcachedClient) cache.getNativeClient();
            Long res = cacheClient.decr(key, 1, -1);
            if (res.equals(-1L)) {
                Long dbRes = defaultCounter.initValue();
                cacheClient.set(key, exp, dbRes.toString());
            }
        } catch (Exception e) {
            log.warn("递减计数器", e);
        }
    }


    /**
     * 提供递增或递减计数器的默认值
     * odb
     */
    public interface DefaultCounter{
        public Long initValue();
    }

}