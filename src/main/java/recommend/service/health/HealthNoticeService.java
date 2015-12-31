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
    private int typeSize = 3;
    private ArrayList<String> esIndexList = Lists.newArrayList("recommend", "recommend", "report", "report", "report");
    private ArrayList<String> esTypeList = Lists.newArrayList("sports", "userstepplan", "body", "step", "sleep");

    @Autowired
    private EsToMemCacheService esToMemCacheService;
    @Autowired
    private IUserService userService;

    public Map getNoticeById(Long id, String timeZone) {

        try {
            int index = new Random().nextInt(typeSize);
            String esIndex = esIndexList.get(index);
            String esType = esTypeList.get(index);
            Map tip = new HashMap();

            if (index == 0) {
                List <String> candidateList = (List <String> )
                        ((HashMap <String, Object>) esToMemCacheService.call(esIndex, esType, id.toString())
                        .get("value")).get("candidates");
                log.info(candidateList.toString());
                tip.put("content", candidateList.get(new Random().nextInt(candidateList.size()))); //new Random().nextInt(content.size())));
            } else if (index == 1) {
                User userProfile = userService.userProfile(id, null);
                Integer stepsPlanTmp = userProfile.getExtension().getDaily_steps();
                Double stepsPlan = 10000D;
                if (stepsPlanTmp != null) {
                    stepsPlan = Double.valueOf(stepsPlanTmp.intValue());
                }
                List tmpContent = (List <List<?> >) ((HashMap<String, Object>) esToMemCacheService.call(esIndex, esType, id.toString())
                        .get("value")).get("stepPlan");

                List content = new ArrayList();
                for(int i = 0; i < tmpContent.size(); i++) {
                    List<?> tmp = (List<?>) tmpContent.get(i);
                    List<Integer> tmpValue = new ArrayList();
                    Double hourStep = stepsPlan * ((Double) tmp.get(1));
                    tmpValue.add(0, (Integer) tmp.get(0));
                    tmpValue.add(1, hourStep.intValue());
                    content.add(i, tmpValue);
                }
                tip.put("content", content.toString());
            } else if (index == 2) {
                User userProfile = userService.userProfile(id, null);
                String gender = userProfile.getSex();
                String birthday = userProfile.getBirthday();
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                Integer age = -1;
                log.info(birthday);
                if (!Strings.isNullOrEmpty(birthday)) {
                    age = Integer.parseInt(df.format(new Date()))
                            - Integer.parseInt(birthday.substring(0, 4));
                }
                String key = gender + "\t" + age.toString();
                Map<String, Object> result = new HashMap<>();

                for (int i = 0; i < 3; i++) {
                    Object content = esToMemCacheService.call(esIndex, esType, key).get("value");
                    esIndex = esIndexList.get(index + i);
                    esType = esTypeList.get(index + i);
                    result.put(esType, content);
                }
                tip.put("content", result);
            }
            Calendar cal  = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone(timeZone);
            cal.setTimeZone(tz);
            tip.put("end_time", System.currentTimeMillis() + 30 * 60 * 10000);
            return tip;
        } catch (Exception e) {
            log.error("notice getting error", e);
            return null;
        }
    }
}
