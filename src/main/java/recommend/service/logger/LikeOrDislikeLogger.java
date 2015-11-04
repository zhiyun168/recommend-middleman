package recommend.service.logger;

import com.zhiyun168.service.api.logger.ILikeOrDislikeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recommend.utils.JsonSerializer;

import java.util.Collections;

/**
 * Created by ouduobiao on 15/11/4.
 */
@Service
public class LikeOrDislikeLogger implements ILikeOrDislikeLogger {
    private static Logger log = LoggerFactory.getLogger(LikeOrDislikeLogger.class);

    public void dislike(Long uid, Long cardId, String collection){
        Data data = new Data(uid, cardId, collection, "dislike");
        log.info(JsonSerializer.serializeAsString(data));
    }

    public void like(Long uid, Long cardId, String collection){
        Data data = new Data(uid, cardId, collection, "like");
        log.info(JsonSerializer.serializeAsString(data));
    }


    private  class Data{
        private Long uid;
        private Long cardId;
        private String collection;
        private String action;
        private long created;

        public Data(Long uid, Long cardId, String collection, String action) {
            this.uid = uid;
            this.cardId = cardId;
            this.collection = collection;
            this.action = action;
            this.created = System.currentTimeMillis();
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public Long getCardId() {
            return cardId;
        }

        public void setCardId(Long cardId) {
            this.cardId = cardId;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
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
