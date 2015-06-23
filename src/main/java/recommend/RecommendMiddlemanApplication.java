package recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import recommend.config.CoreConfig;
import recommend.mapper.TestMapper;
import recommend.service.Loader;
import recommend.service.api.ITestService;


import java.util.Arrays;

@SpringBootApplication
public class RecommendMiddlemanApplication {


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RecommendMiddlemanApplication.class, args);
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }


        //test data base
        TestMapper testMapper = ctx.getBean(TestMapper.class);

        System.out.println(testMapper.getUser());


        //test redis
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate)ctx.getBean("stringRedisTemplate");

        stringRedisTemplate.opsForValue().set("test", "odb set");

        System.out.println(stringRedisTemplate.opsForValue().get("test"));

        //test dubbo
        ITestService test = (ITestService)ctx.getBean("testConsumer");
        System.out.println(test.getName());

        Loader loader = ctx.getBean(Loader.class);
        System.out.println(loader.getRecUser(175553l));


    }
}
