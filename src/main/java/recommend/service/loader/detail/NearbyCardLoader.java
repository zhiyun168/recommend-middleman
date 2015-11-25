package recommend.service.loader.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class NearbyCardLoader extends WithReasonLoader {

    protected static Logger log = LoggerFactory.getLogger(NearbyCardLoader.class);

    public static String REC_NAME = "nearby_card";

    @Override
    public String getEsIndexName() {
        return "recommendation";
    }

    @Override
    public String getEsType() {
        return "nearby_card";
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
