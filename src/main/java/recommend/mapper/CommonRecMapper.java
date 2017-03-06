package recommend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import recommend.model.HealthTipData;
import recommend.model.RecItem;

import java.util.List;

/**
 * Created by ouduobiao on 2017/1/10.
 */
public interface CommonRecMapper {

    @Select("select id, tip,url from home_page_textbar where uid=#{uid} and is_del=0 order by update_time desc limit 1")
    @ResultType(HealthTipData.class)
    HealthTipData getHealthTipCandidate(@Param("uid")Long uid);



    @Select("select banner_id from home_page_popup where uid=#{uid} and is_del=0 order by update_time desc limit 1")
    @ResultType(HealthTipData.class)
    String getLaunchAdCandidate(@Param("uid")Long uid);

    @Select("select tag as candidate, score from user_tag where uid=#{uid}")
    @ResultType(RecItem.class)
    List<RecItem> getUserTag(@Param("uid")String uid);


    @Select("select candidate from user_banner where id=#{id}")
    @ResultType(String.class)
    String getBannerCandidate(@Param("id")String id);

}
