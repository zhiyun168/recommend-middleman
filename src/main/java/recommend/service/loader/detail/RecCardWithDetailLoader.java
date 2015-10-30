package recommend.service.loader.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ouduobiao on 15/10/30.
 */
@Service
public class RecCardWithDetailLoader extends WithReasonLoader{

    protected static Logger log = LoggerFactory.getLogger(RecCardWithDetailLoader.class);

    private static String ES_Index = "recommendation_card_with_detail_alias";
    private static String ES_Type = "CARD";
    private static String ES_ID_FIELD = "user";

    private static String REC_NAME = "card_with_detail";


    @Override
    public String getEsIndexName() {
        return ES_Index;
    }

    @Override
    public String getEsType() {
        return ES_Type;
    }

    @Override
    public String getEsIdField() {
        return ES_ID_FIELD;
    }

    @Override
    public String recName() {
        return REC_NAME;
    }

}
