package com.zhiyun168.service.api.recommend.v2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ouduobiao on 2017/2/27.
 */
public interface IUserTagRecommender {
    class UserTag implements Serializable {
        private String tag;
        private Double score;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }
    }


    List<UserTag> getUserTag(String uid, int maxSize);

}
