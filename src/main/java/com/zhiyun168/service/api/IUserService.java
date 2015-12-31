package com.zhiyun168.service.api;
import java.util.Map;

import com.zhiyun168.model.User;

/**
 * Created by Administrator on 2014/7/14.
 */
public interface IUserService {

    public String getSecurityCode(String mobile,String serviceCode, Boolean requireNull);
    public String getSecurityCodeWithoutSendMsg(String mobile,String serviceCode, Boolean requireNull);

    public int checkSecurityCode(String mobile, String inCode, String serviceCode);

    public int isValidNick(String nick);

    public int isUniqueNick(String nick);

    public int isUniqueMobile(String mobile);

    public Long createUser(User user,String code, String imageType);

    public Long createUser(User user, String imageType);

    public User userProfile(Long userId,User currentUser);

    /**
     * 获取用户信息 protect是否保护隐私信息
     * @param userId
     * @param protect
     * @return
     */
    public User userProfile(Long userId, boolean protect);

    public User userProfile(Long userId);

    /**
     * 获取用户的详情信息
     * @param userId
     * @return
     */
    public User getUserDetailedInfo(Long userId);
    /**
     * 通过用户id获取用户的基本信息
     * @param userId
     * @return
     */
    public Map getUserBaseInfo(Long userId);

    public void updateUserProfile(User user,String imageType);

    public User getUserByMobileAndPassword(String mobile , String password);

    public User getUserByNickName(String nick);

    public Long getUserIdByNickName(String nick);

    /**
     * 修改用户的密码，可用来重置密码
     * @param mobile
     * @param password
     * @return
     */
    public int updatePasswordByMobile( String mobile , String password );

    public void resetPwd(String mobile, String code, String pwd);
    /**
     * 处理绑定手机号的服务
     * @param uid
     * @param mobile
     * @param code
     * @param pwd
     */
    public void bindPhone(Long uid , String mobile, String code, String pwd);

    /**
     * 用户发布的卡片数
     * @param uid
     * @return
     */
    public Long getUserCardsCount(Long uid);

    /**
     * 用户被点赞数
     * @param uid
     * @return
     */
    public Long getUserVeryCount(Long uid);

    /**
     * 设置用户的关注数
     * @param user
     * @return
     */
    public IUserService setLeaders(User user);

    /**
     * 设置用户的粉丝数
     * @param user
     * @return
     */
    public IUserService setFollowers(User user);

    /**
     * 设置用户的发布数
     */
    public IUserService setCardCount(User user);

    /**
     * 设置用户的关注
     * @param user
     * @param currentId
     * @return
     */
    public IUserService setFollow(User user, Long currentId);

    /**
     * 验证用户的token是否合法
     * @param uid
     * @param token
     * @return
     */
    public boolean isValidateUserToken(Long uid, String token);

    /**
     * 封禁用户
     * @param uid
     * @return
     */
    public Integer deactivateUser(Long uid);

    /**
     * 解禁用户
     * @param uid
     * @return
     */
    public Integer activateUser(Long uid);

    /**
     * 更新用户的认证信息
     * @param user
     * @return
     */
    public int updateUserVerifiedInfo(User user);

    /**
     * 获取用户最后登录时间
     * @param uid
     * @return
     */
    public Long getLastLogin(Long uid);
}
