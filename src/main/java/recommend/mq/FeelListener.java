package recommend.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.zhiyun168.service.api.ICardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommend.service.loader.*;
import recommend.service.loader.detail.*;
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
    private ICardService cardService;

    @Autowired
    private RecUserLoader recUserLoader;
    @Autowired
    private RecGoalLoader recGoalLoader;
    @Autowired
    private RecCardLoader recCardLoader;
    @Autowired
    private RecCardWithDetailLoader recCardWithDetailLoader;
    @Autowired
    private JoinedGoalCardLoader joinedGoalCardLoader;
    @Autowired
    private JoinedGoalCardWithDetailLoader joinedGoalCardWithDetailLoader;

    @Autowired
    private SimilarTagCardLoader similarTagCardLoader;
    @Autowired
    private SimilarUserCardLoader similarUserCardLoader;
    @Autowired
    private SimilarUserCardWithDetailLoader similarUserCardWithDetailLoader;
    @Autowired
    private SimilarTagCardWithDetailLoader similarTagCardWithDetailLoader;

    @Autowired
    private GenderGoalWithDetailLoader genderGoalWithDetailLoader;
    @Autowired
    private GoalCommonUserGoalWithDetailLoader goalCommonUserGoalWithDetailLoader;

    @Autowired
    private NearbyCardLoader nearbyCardLoader;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for(MessageExt msg : msgs){

            try{
                String tag = msg.getTags();
                if("addFollow".equals(tag)) {
                    handleAddFollow(msg);
                }
                else if ("join".equals(tag)) {
                    handleJoinGoal(msg);
                }
                else if ("veryCard".equals(tag)) {
                    //log.info("veryCard");
                    handleVeryCard(msg);
                }
                else {
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
        recUserLoader.deleteCandidatesExt(follower, leader);
    }

    private void handleJoinGoal(MessageExt msg)
    {
        String[] gid_uid = msg.getKeys().split(":");

        Long uid = Long.valueOf(gid_uid[1]);
        Long gid = Long.valueOf(gid_uid[0]);

        recGoalLoader.deleteCandidatesExt(uid,gid);

        genderGoalWithDetailLoader.deleteCandidatesExt(uid,gid);
        goalCommonUserGoalWithDetailLoader.deleteCandidates(uid,gid);
    }


    private void handleVeryCard(MessageExt msg)
    {
        byte[] body = msg.getBody();
        Map very = (Map) ObjectUtil.byteToObject(body);

        Long uid = (Long)very.get("user_id");
        Long card_id = (Long)very.get("card_id");
        if(uid == null || card_id ==null)
            return;
        //log.info("{}:{}", uid, card_id);

        recCardLoader.deleteCandidatesExt(uid,card_id);
        recCardWithDetailLoader.deleteCandidatesExt(uid,card_id);

        joinedGoalCardLoader.deleteCandidatesExt(uid,card_id);
        joinedGoalCardWithDetailLoader.deleteCandidatesExt(uid, card_id);

        similarUserCardLoader.deleteCandidatesExt(uid,card_id);
        similarUserCardWithDetailLoader.deleteCandidatesExt(uid,card_id);
        nearbyCardLoader.deleteCandidatesExt(uid,card_id);


        Map card = cardService.findCardById(card_id);
        if(card == null)
            return;
        
        List<Map> tags = (List<Map>)card.get("tags");
        if(tags!=null&&!tags.isEmpty())
        {
            for(Map tag : tags)
            {
                Long bid = (Long)tag.get("bid");
                if(bid==null)
                    continue;

                similarTagCardLoader.deleteCandidatesExt(bid, card_id);
                similarTagCardWithDetailLoader.deleteCandidatesExt(bid,card_id);
            }
        }

    }

}
