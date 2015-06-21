package recommend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;



/**
 * Created by Administrator on 2014/7/10.
 */
@Configuration
@ImportResource( { "classpath*:/spring-context.xml",
        "classpath*:/dubbo-conf/*.xml"
} )
public class CoreConfig {
}
