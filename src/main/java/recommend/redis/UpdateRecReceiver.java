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
        String lockKey = CacheKeyHelper.recLoadDelLockKey(msg);
        boolean isLock = RedisTemplatePlus.set(stringRedisTemplate,
                lockKey, "", "NX", "EX", 60);
        if(isLock)
        {
            try {
                if("ALSO_FOLLOWING".equals(msg))
                {
                    updateAlsoFollowing();
                }
                else if("GENDER_GOAL".equals(msg)) {
                    updateGenderGoal();
                }
                else if("FOLLOWING_USER_LIKED_CARDS".equals(msg))
                {
                    updateFollowingUserLikedCards();
                }
                else
                {
                    log.warn("未知更新消息："+msg);
                }
            }
            catch (Exception e)
            {
                log.error("删除推荐缓存失败:"+msg, e);
            }
            finally {
                stringRedisTemplate.delete(lockKey);
            }
        }
        log.info("end");
    }

    private void updateAlsoFollowing()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_LOAD_KEY);
    }

    private void updateGenderGoal()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_GOAL_LOAD_KEY);
    }

    private void updateFollowingUserLikedCards()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_CARD_LOAD_KEY);
    }

}
