package recommend.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhiyun168.service.api.recommend.IRecommendFeedbackLogger;
import org.springframework.stereotype.Service;
import recommend.service.loader.Loader;
import recommend.service.loader.RecCardLoader;
import recommend.service.loader.RecGoalLoader;
import recommend.service.loader.RecUserLoader;
import recommend.utils.JsonSerializer;

import java.util.Collections;
import java.util.List;

/**
 * Created by ouduobiao on 15/7/29.
 */
@Service
public class RecommendFeedbackLogger implements IRecommendFeedbackLogger {

    private static Logger log = LoggerFactory.getLogger(RecommendFeedbackLogger.class);

    @Autowired
    private RecCardLoader recCardLoader;
    @Autowired
    private RecUserLoader recUserLoader;
    @Autowired
    private RecGoalLoader recGoalLoader;

    private final static String USER = "user";
    private final static String CARD = "card";
    private final static String GOAL = "goal";


    private Loader selectLoader(String type)
    {
        Loader loader = null;
        if(USER.equals(type))
        {
            loader = recUserLoader;
        }
        else if(CARD.equals(type))
        {
            loader = recCardLoader;
        }
        else if(GOAL.equals(type))
        {
            loader = recGoalLoader;
        }

        return loader;
    }

    @Override
    public void like(String type, String uid, String itemId) {

        if(Strings.isNullOrEmpty(uid)|| Strings.isNullOrEmpty(itemId))
            return;

        Loader loader = selectLoader(type);
        if(loader!=null)
        {
            String sub_type = loader.getEsType();
            Data data = new Data(type, sub_type, uid, Collections.singletonList(itemId),
                    "like");
            log.info(JsonSerializer.serializeAsString(data));
        }
    }

    @Override
    public void dislike(String type, String uid, String itemId) {
        if(Strings.isNullOrEmpty(uid)|| Strings.isNullOrEmpty(itemId))
            return;

        Loader loader = selectLoader(type);
        if(loader!=null)
        {
            String sub_type = loader.getEsType();
            Data data = new Data(type, sub_type, uid, Collections.singletonList(itemId),
                    "dislike");
            loader.deleteCandidates(Long.parseLong(uid), Long.parseLong(itemId));
            log.info(JsonSerializer.serializeAsString(data));
        }
    }

    @Override
    public void view(String type, String uid, List<String> itemIds) {

        if(Strings.isNullOrEmpty(uid)|| itemIds == null || itemIds.isEmpty())
            return;

        Loader loader = selectLoader(type);
        if(loader!=null)
        {
            String sub_type = loader.getEsType();
            Data data = new Data(type, sub_type, uid, itemIds,
                    "view");
            log.info(JsonSerializer.serializeAsString(data));
        }

    }


    class Data{
        private String type;
        private String sub_type;
        private String uid;
        private List<String> candidates;
        private String action;
        private long created;

        public Data(String type, String sub_type, String uid, List<String> candidates, String action) {
            this.type = type;
            this.sub_type = sub_type;
            this.uid = uid;
            this.candidates = candidates;
            this.action = action;
            this.created = System.currentTimeMillis();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSub_type() {
            return sub_type;
        }

        public void setSub_type(String sub_type) {
            this.sub_type = sub_type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public List<String> getCandidates() {
            return candidates;
        }

        public void setCandidates(List<String> candidates) {
            this.candidates = candidates;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }
    }

}
