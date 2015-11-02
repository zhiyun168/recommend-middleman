package recommend.service.loader.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.service.loader.Loader;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class SimilarUserCardWithDetailLoader extends WithReasonLoader {

    protected static Logger log = LoggerFactory.getLogger(SimilarUserCardWithDetailLoader.class);

    public static String REC_NAME = "similar_user_card_with_detail";

    @Override
    public String getEsIndexName() {
        return "recommendation";
    }

    @Override
    public String getEsType() {
        return "similar_user_card_with_detail";
    }

    @Override
    public String getEsIdField() {
        return "user";
    }

    @Override
    public String recName() {
        return REC_NAME;
    }

}
