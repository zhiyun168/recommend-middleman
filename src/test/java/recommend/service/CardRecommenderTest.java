package recommend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import recommend.RecommendMiddlemanApplication;
import recommend.service.loader.RecCardLoader;

import java.util.List;


/**
 * Created by ouduobiao on 15/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RecommendMiddlemanApplication.class})
public class CardRecommenderTest {

    @Autowired
    private CardRecommender cardRecommender;

    //@Test
    public void loadFromStorageTest()
    {
        Long uid = 1082l;
        for(int i = 1; i <=10; ++i )
        {
            List<String> rec = cardRecommender.loadFromStorage(uid, i, 10);
            System.out.println(rec);
        }
    }


    @Test
    public void loadFromCacheTest()
    {
        Long uid = 1082l;
        for(int i = 1; i <=10; ++i )
        {
            List<String> rec = cardRecommender.loadFromCache(uid, i, 10);
            System.out.println(rec);
        }
    }


    //@Test
    public void getCandidatesTest()
    {
        Long uid = 1082l;
        for(int i = 1; i <=10; ++i )
        {
            List<String> rec = cardRecommender.getCandidates(uid, i, 10);
            System.out.println(rec);
        }

        List<String> rec = cardRecommender.getCandidates(uid, 0, 10);
        System.out.println(rec);
    }




}


