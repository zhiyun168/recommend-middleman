package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class RecCardLoader extends Loader {

    protected static Logger log = LoggerFactory.getLogger(RecCardLoader.class);

    private static String ES_Type = "followingUserLikedCard";

    @Override
    public String recKey(Long uid) {
        return CacheKeyHelper.recCardKey(uid);
    }

    @Override
    public String recTmpKey(Long uid) {
        return CacheKeyHelper.recTmpCardKey(uid);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_CARD_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long uid) {
        return CacheKeyHelper.recCardLockKey(uid);
    }

    @Override
    protected String getEsType() {
        return ES_Type;
    }


}
