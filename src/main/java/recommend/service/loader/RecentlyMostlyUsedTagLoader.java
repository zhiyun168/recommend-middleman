package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class RecentlyMostlyUsedTagLoader extends Loader {

    private static Logger log = LoggerFactory.getLogger(RecentlyMostlyUsedTagLoader.class);

    private static String ES_Index = "recommendation_tag";
    private static String ES_Type = "recentlyMostlyUsed";
    private static String ES_ID_FIELD = "user";


    @Override
    public String getEsIndexName() {
        return ES_Index;
    }

    @Override
    public String recKey(Long id) {
        return CacheKeyHelper.recentlyMostlyUsedTagKey(id);
    }

    @Override
    public String recTmpKey(Long id) {
        return CacheKeyHelper.tmpRecentlyMostlyUsedTagKey(id);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_RECENTLY_MOSTLY_USED_TAG_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long id) {
        return CacheKeyHelper.recentlyMostlyUsedTagLockKey(id);
    }

    @Override
    public String getEsType() {
        return ES_Type;
    }

    @Override
    public String getEsIdField() {
        return ES_ID_FIELD;
    }
}
