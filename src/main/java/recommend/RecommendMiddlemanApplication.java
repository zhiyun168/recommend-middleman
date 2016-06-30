package recommend;

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

        log.info("recommend middleman start!");
    }
}
