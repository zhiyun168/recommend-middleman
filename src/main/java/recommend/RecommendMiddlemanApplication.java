package recommend;

import org.redisson.Redisson;
import org.redisson.core.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import recommend.redisson.UpdateRecMessageListener;
import recommend.service.api.IUserRecommender;


import java.util.Arrays;




@SpringBootApplication
public class RecommendMiddlemanApplication {
    private static Logger log = LoggerFactory.getLogger(RecommendMiddlemanApplication.class);


    public static void main(String[] args) {

        ApplicationContext ctx = SpringApplication.run(RecommendMiddlemanApplication.class, args);

        log.info("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            log.info(beanName);
        }


        /*
        IUserRecommender rec = (IUserRecommender)ctx.getBean("rec");
        System.out.println(rec.getRandomCandidates(1082l,3));
        */

        //监听索引更新
        Redisson redisson = ctx.getBean(Redisson.class);
        UpdateRecMessageListener listener = ctx.getBean(UpdateRecMessageListener.class);
        RTopic<String> topic =redisson.getTopic("recommendation");
        topic.addListener(listener);
        log.info("recommend middleman start!");
    }
}
