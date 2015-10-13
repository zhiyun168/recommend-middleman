package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class SimilarUserCardLoader extends Loader {

    protected static Logger log = LoggerFactory.getLogger(SimilarUserCardLoader.class);


    private static String ES_Type = "similar_user_card";
    private static String ES_ID_FIELD = "user";


    @Override
    public String recKey(Long id) {
        return CacheKeyHelper.similarUserCardKey(id);
    }

    @Override
    public String recTmpKey(Long id) {
        return CacheKeyHelper.tmpSimilarUserCardKey(id);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_SIMILAR_USER_CARD_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long id) {
        return CacheKeyHelper.similarUserCardLockKey(id);
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
