package recommend.service.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/7/13.
 */
@Service
public class NewUserGenderContextLoader extends Loader {

    protected static Logger log = LoggerFactory.getLogger(NewUserGenderContextLoader.class);


    private static String ES_Type = "newUserGenderContext";
    private static String ES_ID_FIELD = "gender";

    @Override
    public String recKey(Long uid) {
        return CacheKeyHelper.newUserGenderContextKey(uid);
    }

    @Override
    public String recTmpKey(Long uid) {
        return CacheKeyHelper.tmpNewUserGenderContextKey(uid);
    }

    @Override
    public String recLoadKey() {
        return CacheKeyHelper.REC_NewUserGenderContext_LOAD_KEY;
    }

    @Override
    public String recLockKey(Long uid) {
        return CacheKeyHelper.newUserGenderContextLockKey(uid);
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
