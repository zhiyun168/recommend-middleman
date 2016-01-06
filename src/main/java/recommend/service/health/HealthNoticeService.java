package recommend.service.health;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zhiyun168.service.api.IUserService;
import com.zhiyun168.service.api.recommend.IHealthNoticeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import com.zhiyun168.model.User;

/**
 * Created by canoe on 12/29/15.
 */
@Service
public class HealthNoticeService implements IHealthNoticeService {

    private Logger log = Logger.getLogger(HealthNoticeService.class);
    private int typeSize = 2;
    private ArrayList<String> esIndexList = Lists.newArrayList("recommend", "recommend", "report", "report", "report");
    private ArrayList<String> esTypeList = Lists.newArrayList("sports", "userstepplan", "body", "step", "sleep");

    @Autowired
    private EsToMemCacheService esToMemCacheService;
    @Autowired
    private IUserService userService;

    @Override
    public Map getNoticeService(Long id, String timeZone) {

        try {
            int index = new Random().nextInt(typeSize);
            String esIndex = esIndexList.get(index);
            String esType = esTypeList.get(index);
            Map tip = new HashMap();

            if (index == 0) {
                List <String> candidateList = (List <String> )
                        ((HashMap <String, Object>) esToMemCacheService.call(esIndex, esType, id.toString())
                        .get("value")).get("candidates");
                tip.put("content", "今天可以玩玩" + candidateList.get(new Random().nextInt(candidateList.size())));
                tip.put("end_time", System.currentTimeMillis() + 30 * 60 * 10000);
                return tip;
            } else if (index == 1) {
                User userProfile = userService.userProfile(id, null);
                Integer stepsPlanTmp = userProfile.getExtension().getDaily_steps();
                Double stepsPlan = 10000D;
                if (stepsPlanTmp != null) {
                    stepsPlan = Double.valueOf(stepsPlanTmp.intValue());
                }
                List planContent = (List <List<?> >) ((HashMap<String, Object>) esToMemCacheService.call(esIndex, esType, id.toString())
                        .get("value")).get("stepPlan");

                Calendar cal  = Calendar.getInstance();
                if (!Strings.isNullOrEmpty(timeZone)) {
                    TimeZone tz = TimeZone.getTimeZone(timeZone);
                    cal.setTimeZone(tz);
                }
                Integer hour = Integer.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                String content = "";
                for (int i = 0; i < planContent.size(); i++) {
                    List<?> tmp = (List<?>) planContent.get(i);
                    Double hourStepTmp = stepsPlan * ((Double) tmp.get(1));

                    Integer planHour = (Integer) tmp.get(0);
                    if (planHour.equals(hour)) {
                        Integer hourStep = hourStepTmp.intValue();
                        content = "建议当前小时内走" + hourStep.toString() + "步，更容易达到今天的目标";
                    }
                }
                if (!Strings.isNullOrEmpty(content)) {
                    tip.put("content", content);
                    return tip;
                } else {
                    return null;
                }
            } else if (index == 2) {
                User userProfile = userService.userProfile(id, null);
                String gender = userProfile.getSex();
                String birthday = userProfile.getBirthday();
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                Integer age = -1;
                if (!Strings.isNullOrEmpty(birthday)) {
                    age = Integer.parseInt(df.format(new Date()))
                            - Integer.parseInt(birthday.substring(0, 4));
                }
                String key = gender + "\t" + age.toString();
                String content = "";
                for (int i = 0; i < 3; i++) {
                    esIndex = esIndexList.get(index + i);
                    esType = esTypeList.get(index + i);

                    Object valueContentObject = esToMemCacheService.call(esIndex, esType, key);

                    if (null != valueContentObject) {
                        if(!Collections.EMPTY_MAP.equals(((HashMap<String, Object>) valueContentObject).get("value"))) {
                            Object valueContent = ((HashMap<String, Object>) ((HashMap<String, Object>)
                                    valueContentObject).get("value")).get("value");
                            if (i == 0) {
                                Integer bodyFatRate = (Double.valueOf(valueContent.toString()).intValue());
                                content += "和你同性别同年龄的人的平均体脂率为" + bodyFatRate.toString() + "\n";
                            } else if (i == 1) {
                                Integer stepNumber = (Double.valueOf(valueContent.toString())).intValue();
                                content += "和你同性别同年龄的人的平均步数为" + stepNumber.toString() + "步\n";
                            } else if (i == 2) {
                                String [] sleepInfo = valueContent.toString().split("\t");
                                Integer shallowSleepNumber = (Double.valueOf(sleepInfo[0])).intValue();
                                content += "和你同性别同年龄的人的平均浅度睡眠时间为" + shallowSleepNumber.toString() + "分钟\n";
                                Integer deepSleepNumber = (Double.valueOf(sleepInfo[1])).intValue();
                                content += "和你同性别同年龄的人的平均深度睡眠时间为" + deepSleepNumber.toString() + "分钟\n";
                            }
                        }
                    }
                }
                if (!Strings.isNullOrEmpty(content)) {
                    tip.put("content", content);
                    tip.put("end_time", System.currentTimeMillis() + 30 * 60 * 10000);
                    return tip;
                } else {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("notice getting error", e);
            return null;
        }
    }
}
