package recommend.service.api;

import java.util.List;

/**
 * Created by ouduobiao on 15/6/24.
 */
public interface IUserRecommender {

    public List<String> getCandidates(Long uid, int page, int pageSize);

    public List<String> getRandomCandidates(Long uid, int maxSize);

}
