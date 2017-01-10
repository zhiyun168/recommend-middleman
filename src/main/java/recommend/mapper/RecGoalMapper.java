package recommend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * Created by ouduobiao on 16/6/28.
 */
public interface RecGoalMapper {
    @Select("select candidate from daka where id=#{id}")
    @ResultType(String.class)
    String getCandidate(@Param("id")String id);


    @Select("select candidate from dakajian where id=#{id}")
    @ResultType(String.class)
    String getSearchRecCandidate(@Param("id")String id);

    @Select("select candidate from daka_relative_jian where id=#{id}")
    @ResultType(String.class)
    String getBannerGoalCandidate(@Param("id")String id);

    @Select("select candidate from user_recommend_jian where id=#{id}")
    @ResultType(String.class)
    String getFocusUserCandidate(@Param("id")String id);


    @Select("select goal_id_list from banner_personalized where uid=#{uid} and goal_id=#{goal_id} limit 1")
    @ResultType(String.class)
    String getPersonalizedBannerCandidate(@Param("uid")Long uid, @Param("goal_id")Long goal_id);

}
