package recommend.service.recommender.detail;

import com.zhiyun168.service.api.recommend.detail.ICardWithDetailRecommender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ouduobiao on 15/10/30.
 */
@Service
public class CardWithDetailRecommender extends PagingWithDetailRecommender implements ICardWithDetailRecommender {
    @Override
    public Map<String, String> getCandidates(Long uid, int page, int pageSize) {
        return getCandidates_(uid, page, pageSize);
    }

    @Override
    public String getLoaderName() {
        return "recCardWithDetailLoader";
    }
}
