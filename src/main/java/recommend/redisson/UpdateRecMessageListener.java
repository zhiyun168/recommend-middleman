package recommend.redisson;

import org.redisson.Redisson;
import org.redisson.core.MessageListener;
import org.redisson.core.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.utils.CacheKeyHelper;

/**
 * Created by ouduobiao on 15/6/25.
 */
@Component
public class UpdateRecMessageListener implements MessageListener<String> {
    private static Logger log = LoggerFactory.getLogger(UpdateRecMessageListener.class);

    @Autowired
    private Redisson redisson;

    @Override
    public void onMessage(String msg) {
        log.info("Received <" + msg + ">");
        RMap<String, String> loadMap = redisson.getMap(CacheKeyHelper.REC_LOAD_KEY);
        loadMap.delete();
        log.info("end");
    }
}
