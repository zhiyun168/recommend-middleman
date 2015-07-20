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
public class RecCardLoaderTest {

    @Autowired
    private RecCardLoader cardLoader;

    @Test
    public void hasLoadToCacheTest()
    {
        boolean load = cardLoader.hasLoadToCache(1082l);
        System.out.println(load);
    }

    //@Test
     public void getCandidatesFromStorageTest()
    {
        List<String> rec= cardLoader.getCandidatesFromStorage(1082l);
        System.out.println(rec);
    }

    //@Test
    public void loadToCacheTest()
    {
        boolean ok= cardLoader.loadToCache(1082l);
        System.out.println(ok);
    }


}


