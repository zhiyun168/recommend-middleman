package recommend.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

/**
 * Created by ouduobiao on 15/6/23.
 */
public class RedisTemplatePlus {

    public static boolean set(RedisTemplate template, final String key, final String value, final String nxxx, final String expx, final int time)
    {
        Object res = template.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                return  jedis.set(key, value, nxxx, expx, time);
            }
        }, true);

        return res != null;
    }
}
