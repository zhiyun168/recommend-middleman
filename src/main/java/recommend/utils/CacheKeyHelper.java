package recommend.utils;

/**
 * Created by ouduobiao on 15/6/23.
 */
public class CacheKeyHelper {

    /**
     * redis key section
     */

    private static final String REC_LOAD_LOCK_KEY_PREFIX = "feel:rec:load:lock";
    private static final String REC_GOAL_LOAD_LOCK_KEY_PREFIX = "feel:rec:goal:load:lock";
    private static final String REC_CARD_LOAD_LOCK_KEY_PREFIX = "feel:rec:card:load:lock";
    private static final String REC_TAG_LOAD_LOCK_KEY_PREFIX = "feel:rec:tag:load:lock";
    private static final String REC_NewUserTagContext_LOAD_LOCK_KEY_PREFIX = "feel:rec:NewUserTagContext:load:lock";
    private static final String REC_NewUserGenderContext_LOAD_LOCK_KEY_PREFIX = "feel:rec:NewUserGenderContext:load:lock";
    private static final String REC_JOINED_GOAL_CARD_LOAD_LOCK_KEY_PREFIX= "feel:rec:recommendation:joined:goal:card:load:lock";
    private static final String REC_RECENTLY_MOSTLY_USED_TAG_LOAD_LOCK_KEY_PREFIX = "feel:rec:recently_mostly_used_tag:load:lock";
    private static final String REC_SIMILAR_TAG_CARD_LOAD_LOCK_KEY_PREFIX = "feel:rec:similar:tag:card:load:lock";
    private static final String REC_SIMILAR_USER_CARD_LOAD_LOCK_KEY_PREFIX = "feel:rec:similar:user:card:load:lock";

    private static final String REC_USER_PREFIX = "feel:rec:user";
    private static final String REC_GOAL_PREFIX = "feel:rec:goal";
    private static final String REC_CARD_PREFIX = "feel:rec:card";
    private static final String REC_USER_FOLLOWED_PREFIX = "feel:rec:user:followed";
    private static final String REC_TAG_PREFIX = "feel:rec:tag";
    private static final String REC_NewUserTagContext_PREFIX = "feel:rec:NewUserTagContext";
    private static final String REC_NewUserGenderContext_PREFIX = "feel:rec:NewUserGenderContext";
    private static final String REC_JOINED_GOAL_CARD_PREFIX = "feel:rec:recommendation:joined:goal:card";
    private static final String REC_RECENTLY_MOSTLY_USED_TAG_PREFIX = "feel:rec:recently_mostly_used_tag";
    private static final String REC_SIMILAR_TAG_CARD_PREFIX = "feel:rec:similar:tag:card";
    private static final String REC_SIMILAR_USER_CARD_PREFIX = "feel:rec:similar:user:card";


    public static final String REC_LOAD_KEY = "feel:rec:load";
    public static final String REC_GOAL_LOAD_KEY = "feel:rec:goal:load";
    public static final String REC_CARD_LOAD_KEY = "feel:rec:card:load";
    public static final String REC_TAG_LOAD_KEY = "feel:rec:tag:load";
    public static final String REC_NewUserTagContext_LOAD_KEY = "feel:rec:NewUserTagContext:load";
    public static final String REC_NewUserGenderContext_LOAD_KEY = "feel:rec:NewUserGenderContext:load";
    public static final String REC_JOINED_GOAL_CARD_LOAD_KEY = "feel:rec:recommendation:joined:goal:card:load";
    public static final String REC_RECENTLY_MOSTLY_USED_TAG_LOAD_KEY = "feel:rec:recently_mostly_used_tag:load";
    public static final String REC_SIMILAR_TAG_CARD_LOAD_KEY = "feel:rec:similar:tag:card:load";
    public static final String REC_SIMILAR_USER_CARD_LOAD_KEY = "feel:rec:similar:user:card:load";


    public static final String HEALTH_USER_SPORTS = "feel:health:sports";
    public static final String HEALTH_USER_REPORT = "feel:health:report";
    public static final String HEALTH_USER_PLAN = "feel:health:plan";
    public static final String LAUNCH_AD_USER = "feel:launch_ad:user";
    public static final String LAUNCH_AD_DAILY_TOTAL = "feel:launch_ad:daily:total";

    /**
     * 加载用户的推荐用户到cache的key
     * @param uid
     * @return
     */
    public static String recLoadLockKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_LOAD_LOCK_KEY_PREFIX, uid);
    }

    public static String recGoalLockKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_GOAL_LOAD_LOCK_KEY_PREFIX, uid);
    }

    public static String recTagLockKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_TAG_LOAD_LOCK_KEY_PREFIX, id);
    }

    public static String recCardLockKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_CARD_LOAD_LOCK_KEY_PREFIX, uid);
    }

    public static String recTmpUserKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_USER_PREFIX,"tmp",uid);
    }

    public static String recTmpGoalKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_GOAL_PREFIX,"tmp",uid);
    }

    public static String recTmpTagKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_TAG_PREFIX,"tmp",id);
    }

    public static String recTmpCardKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_CARD_PREFIX,"tmp",uid);
    }

    public static String recUserKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_USER_PREFIX, uid);
    }

    public static String recGoalKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_GOAL_PREFIX, uid);
    }

    public static String recTagKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_TAG_PREFIX, id);
    }

    public static String recCardKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_CARD_PREFIX, uid);
    }

    public static String recFollowedUser(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_USER_FOLLOWED_PREFIX, uid);
    }

    public static String recLoadDelLockKey(String recType)
    {
        return StringHelper.DotJoiner.join(REC_LOAD_KEY, "lock", recType);
    }


    public static String newUserTagContextKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_NewUserTagContext_PREFIX, id);
    }

    public static String tmpNewUserTagContextKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_NewUserTagContext_PREFIX,"tmp",id);
    }

    public static String newUserTagContextLockKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_NewUserTagContext_LOAD_LOCK_KEY_PREFIX, id);
    }

    public static String newUserGenderContextKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_NewUserGenderContext_PREFIX, id);
    }

    public static String tmpNewUserGenderContextKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_NewUserGenderContext_PREFIX,"tmp",id);
    }

    public static String newUserGenderContextLockKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_NewUserGenderContext_LOAD_LOCK_KEY_PREFIX, id);
    }

    public static String recJoinedGoalCardKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_JOINED_GOAL_CARD_PREFIX, uid);
    }

    public static String recTmpJoinedGoalCardKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_JOINED_GOAL_CARD_PREFIX,"tmp",uid);
    }

    public static String recJoinedGoalCardLockKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_JOINED_GOAL_CARD_LOAD_LOCK_KEY_PREFIX, uid);
    }

    public static String recentlyMostlyUsedTagKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_RECENTLY_MOSTLY_USED_TAG_PREFIX, id);
    }

    public static String tmpRecentlyMostlyUsedTagKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_RECENTLY_MOSTLY_USED_TAG_PREFIX, "tmp", id);
    }

    public static String recentlyMostlyUsedTagLockKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_RECENTLY_MOSTLY_USED_TAG_LOAD_LOCK_KEY_PREFIX, id);
    }


    public static String similarTagCardKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_SIMILAR_TAG_CARD_PREFIX, id);
    }
    public static String tmpSimilarTagCardKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_SIMILAR_TAG_CARD_PREFIX,"tmp",id);
    }
    public static String similarTagCardLockKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_SIMILAR_TAG_CARD_LOAD_LOCK_KEY_PREFIX, id);
    }

    public static String similarUserCardKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_SIMILAR_USER_CARD_PREFIX, id);
    }
    public static String tmpSimilarUserCardKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_SIMILAR_USER_CARD_PREFIX,"tmp",id);
    }
    public static String similarUserCardLockKey(Long id)
    {
        return StringHelper.DotJoiner.join(REC_SIMILAR_USER_CARD_LOAD_LOCK_KEY_PREFIX, id);
    }

    public static String healthUserReport(Long id) {
        return StringHelper.DotJoiner.join(HEALTH_USER_REPORT, id);
    }

    public static String healthUserSports(Long id) {
        return StringHelper.DotJoiner.join(HEALTH_USER_SPORTS, id);
    }

    public static String healthUserPlan(Long id) { return StringHelper.DotJoiner.join(HEALTH_USER_PLAN, id); }
    /**
     * memcached key section
     */


}
