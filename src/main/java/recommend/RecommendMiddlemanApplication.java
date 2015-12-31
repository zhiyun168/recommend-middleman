package recommend;

import com.zhiyun168.service.api.recommend.IHealthNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;



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




        //IUserRecommender rec = (IUserRecommender)ctx.getBean("rec");
        //System.out.println(rec.getRandomCandidates(1082l,3));
        //System.out.println(rec.getRandomCandidates(1082l,3));

        //GoalRecommender goalRecommender = ctx.getBean(GoalRecommender.class);
        //System.out.println(goalRecommender.getCandidates(1082l,3));
        //System.out.println(goalRecommender.getCandidates(1082l,3));

        //IGoalService goalService = ctx.getBean(IGoalService.class);

        //System.out.println(goalService.isJoinGoal(13l,10l));

        //TagRecommender tagRecommender = ctx.getBean(TagRecommender.class);
        //System.out.println(tagRecommender.getCandidates(15204l, 1, 2));
        //System.out.println(tagRecommender.getCandidates(66377l, 1, 2));


        /*
        //监听索引更新
        Redisson redisson = ctx.getBean(Redisson.class);
        UpdateRecMessageListener listener = ctx.getBean(UpdateRecMessageListener.class);
        RTopic<String> topic =redisson.getTopic("recommendation");
        topic.addListener(listener);
        */
        IHealthNoticeService healthNoticeService = ctx.getBean("ab", IHealthNoticeService.class);
        System.out.println(healthNoticeService.getNoticeById(1088L, "GMT+8"));

        log.info("recommend middleman start!");
    }
}
