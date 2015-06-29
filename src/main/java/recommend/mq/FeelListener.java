package recommend.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.redisson.Redisson;
import org.redisson.core.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.service.Loader;
import recommend.utils.CacheKeyHelper;
import recommend.utils.ObjectUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/6/25.
 */

@Component
public class FeelListener implements MessageListenerConcurrently {

    private static Logger log = LoggerFactory.getLogger(FeelListener.class);

    @Autowired
    private Loader loader;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for(MessageExt msg : msgs){
            byte[] body = msg.getBody();
            try{
                Map followInfo = (Map) ObjectUtil.byteToObject(body);
                Long leader = (Long) followInfo.get("leader");
                Long follower = (Long) followInfo.get("follower");
                if(loader.hasLoadToCache(follower))
                {
                    log.info("del rec user 1-follower:{},leader:{}",follower,leader);
                    loader.deleteRecUser(follower, leader);
                }
                else
                {
                    boolean hasLoad = loader.loadToCache(follower);
                    if(hasLoad)
                    {
                        log.info("del rec user 2-follower:{},leader:{}",follower,leader);
                        loader.deleteRecUser(follower, leader);
                    }

                    else
                    {
                        log.info("add followed user 2-follower:{},leader:{}",follower,leader);
                        loader.addFollowedRecUser(follower, leader);
                    }

                }
            }
            catch (Exception e)
            {
                //只消费一次 无论失败与否
                log.error("处理消息失败:"+ msg.toString(), e);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
