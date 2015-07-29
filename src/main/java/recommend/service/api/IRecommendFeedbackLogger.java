package recommend.service.api;

import java.util.List;

/**
 * Created by ouduobiao on 15/7/29.
 */
public interface IRecommendFeedbackLogger {
    void like(String type, String uid, String itemId);
    void dislike(String type, String uid, String itemId);
    void view(String type, String uid, List<String> itemIds);
}
