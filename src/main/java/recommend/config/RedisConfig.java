package recommend.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import recommend.redis.UpdateRecReceiver;
/**
 * Created by ouduobiao on 15/6/21.
 */
@Configuration
public class RedisConfig {

    private static Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Value("${redis.address}") String address)
    {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        log.info("redis address:" + address);
        String[] host_port = address.split(":");
        factory.setHostName(host_port[0]);
        if(host_port.length == 2)
            factory.setPort(Integer.valueOf(host_port[1]));
        factory.setUsePool(true);
        return factory;
    }



    @Bean
    StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("recommendation"));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(UpdateRecReceiver updateRecReceiver) {
        return new MessageListenerAdapter(updateRecReceiver, "receiveMessage");
    }

}
