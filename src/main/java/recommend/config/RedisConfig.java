package recommend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by ouduobiao on 15/6/21.
 */
@Configuration
@ImportResource( {
        "classpath*:/spring-data-redis.xml"
} )
public class RedisConfig {

}
