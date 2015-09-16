package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class NewUserTagContextLoader extends Loader {

    private static Logger log = LoggerFactory.getLogger(NewUserTagContextLoader.class);

    private static String ES_Type = "newUserTagContext";
    private static String ES_ID_FIELD = "tag";


    @Override
    public String recKey(Long id) {
        return CacheKeyHelper.newUserTagContextKey(id);
    }

    @Override
    public String recTmpKey(Long id) {
        return CacheKeyHelper.tmpNewUserTagContextKey(id);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_NewUserTagContext_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long id) {
        return CacheKeyHelper.newUserTagContextLockKey(id);
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
