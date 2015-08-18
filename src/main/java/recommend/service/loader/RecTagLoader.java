package recommend.service.loader;

import com.zhiyun168.service.api.goal.IGoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class RecTagLoader extends Loader {

    private static Logger log = LoggerFactory.getLogger(RecTagLoader.class);

    private static String ES_Type = "similarTag";
    private static String ES_ID_FIELD = "tag";


    @Override
    public String recKey(Long id) {
        return CacheKeyHelper.recTagKey(id);
    }

    @Override
    public String recTmpKey(Long id) {
        return CacheKeyHelper.recTmpTagKey(id);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_TAG_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long id) {
        return CacheKeyHelper.recTagLockKey(id);
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
