package recommend.service.recommender.detail;

import com.zhiyun168.service.api.recommend.detail.ISimilarTagCardWithDetailRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/7/20.
 */
@Service
public class SimilarTagCardWithDetailRecommender extends PagingWithDetailRecommender implements ISimilarTagCardWithDetailRecommender {

    private static Logger log = LoggerFactory.getLogger(SimilarTagCardWithDetailRecommender.class);

    @Override
    public Map<String, String> getCandidates(Long id, int page, int pageSize) {
        return getCandidates_(id, page, pageSize);
    }

    @Override
    public Map<String, String> getCandidatesWithDetail(List<Long> ids, int page, int pageSize) {
        int idCount = ids.size();
        if(idCount == 0)
        {
            return Collections.EMPTY_MAP;
        }
        else
        {
            if(idCount > pageSize)
            {
                idCount = pageSize;
            }

            int pageSizePerId = pageSize / idCount;
            int rem = pageSize - idCount * pageSizePerId;
            Map<String, String> res = new LinkedHashMap<>(pageSize);
            int size = pageSizePerId + rem;
            for(Long id : ids.subList(0, idCount))
            {
                Map<String, String> cardWithReasons = getCandidates(id, page, size);
                res.putAll(cardWithReasons);
                size = pageSizePerId+(size-cardWithReasons.size());
            }
            return res;
        }
    }

    @Override
    public String getLoaderName() {
        return "similarTagCardWithDetailLoader";
    }
}
