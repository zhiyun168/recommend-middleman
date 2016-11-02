package recommend;

import com.zhiyun168.service.api.recommend.v2.IBannerGoalRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


import java.util.Arrays;
import java.util.List;


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


        IBannerGoalRecommender  bannerGoalRecommender = ctx.getBean(IBannerGoalRecommender.class);
        List<String> r  =  bannerGoalRecommender.getCandidates("1005" , 1, 10);
        log.info(r.toString());

        log.info("recommend middleman start!");
    }
}
