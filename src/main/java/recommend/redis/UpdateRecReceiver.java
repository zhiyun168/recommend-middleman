package recommend.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import recommend.service.loader.KeyBuilder;
import recommend.service.loader.detail.*;
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
                if("USER".equals(msg))
                {
                    updateUser();
                }
                else if("GENDER_GOAL".equals(msg)) {
                    updateGenderGoal();
                }
                else if("CARD".equals(msg))
                {
                    updateCard();
                }
                else if("TAG_RECOMMENDATION_DONE".equals(msg))
                {
                    updateTagRecommendation();
                }
                else if("NEW_USER_TAG_GOOD_CONTEXT".equals(msg))
                {
                    updateNewUserTagContext();
                }
                else if("NEW_USER_GENDER_GOOD_CONTEXT".equals(msg))
                {
                    updateNewUserGenderContext();
                }
                else if("JOINED_SAME_GOAL_USER_CARD".equals(msg))
                {
                    updateJoinedGoalCard();
                }
                else if("RECENTLY_MOSTLY_USED_TAG".equals(msg))
                {
                    updateRecentlyMostlyUsedTag();
                }
                else if("SIMILAR_TAG_CARD".equals(msg))
                {
                    updateSimilarTagCard();
                }
                else if("SIMILAR_USER_CARD".equals(msg))
                {
                    updateSimilarUserCard();
                }
                else if("CARD_WITH_DETAIL".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(RecCardWithDetailLoader.REC_NAME));
                }
                else if("JOINED_SAME_GOAL_USER_CARD_WITH_DETAIL".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(JoinedGoalCardWithDetailLoader.REC_NAME));
                }
                else if("SIMILAR_USER_CARD_WITH_DETAIL".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(SimilarUserCardWithDetailLoader.REC_NAME));
                }
                else if("SIMILAR_TAG_CARD_WITH_DETAIL".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(SimilarTagCardWithDetailLoader.REC_NAME));
                }
                else if("GENDER_GOAL_WITH_DETAIL".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(GenderGoalWithDetailLoader.REC_NAME));
                }
                else if("COMMON_GOAL_USER_GOAL".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(GoalCommonUserGoalWithDetailLoader.REC_NAME));
                }
                else if("NEARBY_USER_CARD".equals(msg))
                {
                    stringRedisTemplate.delete(KeyBuilder.recLoadKey(NearbyCardLoader.REC_NAME));
                }
                else if("USER_SPORTS".equals(msg))
                {
                    stringRedisTemplate.delete("feel_health_recommend_sports");
                }
                else if ("USER_REPORT_BODY".equals(msg))
                {
                    stringRedisTemplate.delete("feel_health_report_body");
                }
                else if ("USER_REPORT_STEP".equals(msg))
                {
                    stringRedisTemplate.delete("feel_health_report_step");
                }
                else if ("USER_REPORT_SLEEP".equals(msg))
                {
                    stringRedisTemplate.delete("feel_health_report_sleep");
                }
                else if ("USER_PLAN".equals(msg))
                {
                    stringRedisTemplate.delete("feel_health_recommend_userstepplan");
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

    private void updateUser()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_LOAD_KEY);
    }

    private void updateGenderGoal()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_GOAL_LOAD_KEY);
    }

    private void updateCard()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_CARD_LOAD_KEY);
    }

    private void updateTagRecommendation()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_TAG_LOAD_KEY);
    }

    private void updateNewUserTagContext()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_NewUserTagContext_LOAD_KEY);
    }

    private void updateNewUserGenderContext()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_NewUserGenderContext_LOAD_KEY);
    }

    private void updateJoinedGoalCard()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_JOINED_GOAL_CARD_LOAD_KEY);
    }

    private void updateRecentlyMostlyUsedTag()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_RECENTLY_MOSTLY_USED_TAG_LOAD_KEY);
    }

    private void updateSimilarTagCard()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_SIMILAR_TAG_CARD_LOAD_KEY);
    }

    private void updateSimilarUserCard()
    {
        stringRedisTemplate.delete(CacheKeyHelper.REC_SIMILAR_USER_CARD_LOAD_KEY);
    }

}
