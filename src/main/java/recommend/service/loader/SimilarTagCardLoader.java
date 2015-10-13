package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class SimilarTagCardLoader extends Loader {

    protected static Logger log = LoggerFactory.getLogger(SimilarTagCardLoader.class);


    private static String ES_Type = "similar_tag_card";
    private static String ES_ID_FIELD = "tag";


    @Override
    public String recKey(Long id) {
        return CacheKeyHelper.similarTagCardKey(id);
    }

    @Override
    public String recTmpKey(Long id) {
        return CacheKeyHelper.tmpSimilarTagCardKey(id);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_SIMILAR_TAG_CARD_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long id) {
        return CacheKeyHelper.similarTagCardLockKey(id);
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
