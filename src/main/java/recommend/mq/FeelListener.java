package recommend.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.service.Loader2;
import recommend.service.RecGoalLoader;
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
    private Loader2 loader2;
    @Autowired
    private RecGoalLoader recGoalLoader;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for(MessageExt msg : msgs){

            try{
                String tag = msg.getTags();
                if("addFollow".equals(tag))
                    handleAddFollow(msg);
                else if ("join".equals(tag))
                    handleJoinGoal(msg);
                else
                {
                    log.warn("未知消息："+ msg.toString());
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


    private void handleAddFollow(MessageExt msg)
    {
        byte[] body = msg.getBody();
        Map followInfo = (Map) ObjectUtil.byteToObject(body);
        Long leader = (Long) followInfo.get("leader");
        Long follower = (Long) followInfo.get("follower");
        if(loader2.hasLoadToCache(follower))
        {
            //log.info("del rec user 1-follower:{},leader:{}",follower,leader);
            loader2.deleteRecUser(follower, leader);
        }
        else
        {
            boolean hasLoad = loader2.loadToCache(follower);
            if(hasLoad)
            {
                //log.info("del rec user 2-follower:{},leader:{}",follower,leader);
                loader2.deleteRecUser(follower, leader);
            }

            else
            {
                //log.info("add followed user 2-follower:{},leader:{}",follower,leader);
                loader2.addFollowedRecUser(follower, leader);
            }

        }
    }

    private void handleJoinGoal(MessageExt msg)
    {
        String[] gid_uid = msg.getKeys().split(":");

        Long uid = Long.valueOf(gid_uid[1]);
        Long gid = Long.valueOf(gid_uid[0]);

        if(recGoalLoader.hasLoadToCache(uid))
        {
            //log.info("del rec user 1-follower:{},leader:{}",follower,leader);
            recGoalLoader.deleteCandidates(uid,gid);
        }
        else
        {

            boolean hasLoad = recGoalLoader.loadToCache(uid);
            if(hasLoad)//
            {
                //log.info("del rec user 2-follower:{},leader:{}",follower,leader);
                recGoalLoader.deleteCandidates(uid,gid);
            }
        }
    }

}
