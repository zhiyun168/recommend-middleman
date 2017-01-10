package recommend.service.recommender.v2.impl;


import com.zhiyun168.service.api.recommend.v2.ILaunchAdRecommender;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import recommend.service.recommender.v2.PagingRecommender;
import recommend.utils.CacheKeyHelper;



import java.util.List;

/**
 * Created by ouduobiao on 2017/1/6.
 */
@Service("launchAdRecommender")
public class LaunchAdRecommender extends PagingRecommender implements ILaunchAdRecommender{
    private static Logger log = LoggerFactory.getLogger(LaunchAdRecommender.class);

    private static Long MaxDailyTotal = 300L;
    private static Long MaxUserTotalWeek = 1L;

    @Override
    public String getAd(String uid) {

        try {
            //每天看到弹出框的总用户数不超过 300次 （增加一个配置，初始配置成300）
            Long total = stringRedisTemplate.opsForHyperLogLog().size(CacheKeyHelper.LAUNCH_AD_DAILY_TOTAL);
            if(total >= MaxDailyTotal)
                return null;
            //每个用户一周最多显示一次启动弹出窗口 （增加一个配置，初始配置成1）
            Object userTotalStr = stringRedisTemplate.opsForHash().get(CacheKeyHelper.LAUNCH_AD_USER, uid);
            if(userTotalStr != null)
            {Long userTotal = Long.parseLong((String)userTotalStr);
                if(userTotal >= MaxUserTotalWeek)
                    return null;
            }



            List<String> strL = getCandidates_(uid, 1, 1);
            if(CollectionUtils.isEmpty(strL))
                return null;
            else
            {

                stringRedisTemplate.opsForHyperLogLog().add(CacheKeyHelper.LAUNCH_AD_DAILY_TOTAL, uid);
                LocalDate now = LocalDate.now();
                //前3次做失效
                if(total < 3)
                {
                    stringRedisTemplate.expireAt(CacheKeyHelper.LAUNCH_AD_DAILY_TOTAL,
                            now.plusDays(1).toDate());
                }

                Long newUserTotal = stringRedisTemplate.opsForHash().increment(CacheKeyHelper.LAUNCH_AD_USER,
                        uid,1L);
                if(newUserTotal == 1L)
                {

                    stringRedisTemplate.expireAt(CacheKeyHelper.LAUNCH_AD_USER,
                            now.plusDays(8-now.getDayOfWeek()).toDate());
                }

                return strL.get(0);
            }
        }
        catch (Exception e)
        {
            log.error("获取弹出框出错", e);
            return null;
        }


    }


    @Override
    public String getLoaderName() {
        return "launchAdLoader";
    }
}
