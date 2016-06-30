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
}