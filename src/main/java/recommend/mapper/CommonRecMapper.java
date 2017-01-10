package recommend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import recommend.model.HealthTipData;

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
}
