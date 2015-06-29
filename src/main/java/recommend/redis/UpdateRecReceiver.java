package recommend.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/6/25.
 */
@Component
public class UpdateRecReceiver {
    private static Logger log = LoggerFactory.getLogger(UpdateRecReceiver.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public void receiveMessage(String msg) {
        log.info("Received <" + msg + ">");
        String lockKey = CacheKeyHelper.recLoadDelLockKey();
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, "", "NX", "EX", 60);
        if(isLock)
        {
            try {
                stringRedisTemplate.delete(CacheKeyHelper.REC_LOAD_KEY);
            }
            catch (Exception e)
            {
                log.error("删除推荐缓存失败:"+CacheKeyHelper.REC_LOAD_KEY, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }
        log.info("end");
    }
}
