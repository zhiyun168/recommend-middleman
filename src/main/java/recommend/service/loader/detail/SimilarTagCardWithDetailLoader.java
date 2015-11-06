package recommend.service.loader.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class SimilarTagCardWithDetailLoader extends WithReasonLoader {

    protected static Logger log = LoggerFactory.getLogger(SimilarTagCardWithDetailLoader.class);

    public static String REC_NAME = "similar_tag_card_with_detail";

    @Override
    public String getEsIndexName() {
        return "recommendation";
    }

    @Override
    public String getEsType() {
        return "similar_tag_card_with_detail";
    }

    @Override
    public String getEsIdField() {
        return "tag";
    }

    @Override
    public String recName() {
        return REC_NAME;
    }

}
