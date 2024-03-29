package recommend.config;


import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import recommend.mq.FeelListener;


/**
 * Created by Administrator on 2014/7/10.
 */
@Configuration
@ImportResource( { "classpath*:/spring-context.xml",
        "classpath*:/dubbo-conf/*.xml"
} )
public class CoreConfig {
    private static Logger log = LoggerFactory.getLogger(CoreConfig.class);

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer consumer(@Value("${mq.consumer_group}") String consumerGroup,
                                          @Value("${mq.name_server_addr}") String nameServerAddr,
                                          FeelListener feelListener) throws MQClientException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(consumerGroup), "consumer_group值不存在");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(nameServerAddr), "name_server_addr值不存在");

        //监听关注人消息
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        consumer.setNamesrvAddr(nameServerAddr);

        consumer.subscribe("Follow", "addFollow");
        consumer.subscribe("Goal", "join");
        consumer.subscribe("Very", "veryCard");

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.registerMessageListener(feelListener);
        return consumer;
    }


}
