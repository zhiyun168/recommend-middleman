package com.zhiyun168.service.api.goal;

public interface IGoalService{
    /**
     *用户是否加入任务
     * @param id
     * @param uid
     * @return
     */
    public Boolean isJoinGoal(Long id, Long uid);

}