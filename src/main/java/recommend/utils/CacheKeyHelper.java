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
    private static final String REC_USER_PREFIX = "feel:rec:user";
    private static final String REC_GOAL_PREFIX = "feel:rec:goal";
    private static final String REC_USER_FOLLOWED_PREFIX = "feel:rec:user:followed";


    public static final String REC_LOAD_KEY = "feel:rec:load";
    public static final String REC_GOAL_LOAD_KEY = "feel:rec:goal:load";


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

    public static String recTmpUserKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_USER_PREFIX,"tmp",uid);
    }

    public static String recTmpGoalKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_GOAL_PREFIX,"tmp",uid);
    }

    public static String recUserKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_USER_PREFIX, uid);
    }

    public static String recGoalKey(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_GOAL_PREFIX, uid);
    }

    public static String recFollowedUser(Long uid)
    {
        return StringHelper.DotJoiner.join(REC_USER_FOLLOWED_PREFIX, uid);
    }

    public static String recLoadDelLockKey(String recType)
    {
        return StringHelper.DotJoiner.join(REC_LOAD_KEY, "lock", recType);
    }


    /**
     * memcached key section
     */


}
