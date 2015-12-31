package com.zhiyun168.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.code.ssm.api.CacheKeyMethod;
import com.zhiyun168.model.user.UserExtension;

import java.io.Serializable;


/**
 * Created by odb on 2014/7/10.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
    private Long id;
    private String nick;
    private String mobile;
    private String avatar;
    private String sex;
    private Long reg_time;
    private String password;
    private String birthday;
    //支持B端 增加字段
    private String weixin_openid;
    private Integer store_id;
    private String data_from;
    //用户基本信息 增加字段
    private Integer follow;//是否关注
    private Integer is_leader;//是否被对方关注了 0:对方没关注自己，1：对方关注了自己
    private Long leaders;//关注数
    private Long followers;//粉丝数
    private  String intro;
    private  String email;
    private  String cover;
    private String weibo;
    private String qq;
    private String weixin;
    private String feeltoken;
    private Long card_count;//创建的卡片
    private Integer is_enable;

    private Integer reg_fresh = 0;//是否新注册用户
    
    private Long verified_group; //是否验证
    private String verified_logo;//验证以后的logo
    private String verified_info;//验证信息
    
    //地区信息
    private String country;//国家
    private String province;//省
    private String city;//城市
    private String county;//县区
    private String location;//地区
    private String often_location;//常出没地
    
    //星座
    private String constellation;
    //环信的用户名密码
    private String hx_user_name;
    private String hx_pwd;

    private Long last_login;
    /**
     * 设备级别
     */
    private Integer device_type = 0;

    /**
     * 用户想要总数
     */
    private Integer wants;


    /**
     * 用户扩展信息
     */
    private UserExtension extension;


    @JsonIgnore
    @CacheKeyMethod
    public String getUsername() {
        //手机号就是用户名
        return this.mobile;
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return is_enable == 1;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        if(nick != null)
            this.nick = nick.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getReg_time() {
        return reg_time;
    }

    public void setReg_time(Long reg_time) {
        this.reg_time = reg_time;
    }

    public String getWeixin_openid() {
        return weixin_openid;
    }

    public void setWeixin_openid(String weixin_openid) {
        this.weixin_openid = weixin_openid;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public String getData_from() {
        return data_from;
    }

    public void setData_from(String data_from) {
        this.data_from = data_from;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }

    public Long getLeaders() {
        return leaders;
    }

    public void setLeaders(Long leaders) {
        this.leaders = leaders;
    }

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getWants() {
        return wants;
    }

    public void setWants(Integer wants) {
        this.wants = wants;
    }

    public Long getCard_count() {
        return card_count;
    }

    public void setCard_count(Long card_count) {
        this.card_count = card_count;
    }

    public String getFeeltoken() {
        return feeltoken;
    }

    public void setFeeltoken(String feeltoken) {
        this.feeltoken = feeltoken;
    }

    public Integer getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(Integer is_enable) {
        this.is_enable = is_enable;
    }

	public Long getVerified_group() {
		return verified_group;
	}

	public void setVerified_group(Long verified_group) {
		this.verified_group = verified_group;
	}

	public String getVerified_logo() {
		return verified_logo;
	}

	public void setVerified_logo(String verified_logo) {
		this.verified_logo = verified_logo;
	}

	public String getVerified_info() {
        return verified_info;
    }

    public void setVerified_info(String verified_info) {
        this.verified_info = verified_info;
    }
    
    public Integer getIs_leader() {
		return is_leader;
	}

	public void setIs_leader(Integer is_leader) {
		this.is_leader = is_leader;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOften_location() {
		return often_location;
	}

	public void setOften_location(String often_location) {
		this.often_location = often_location;
	}
	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	
	public String getHx_user_name() {
		return hx_user_name;
	}

	public void setHx_user_name(String hx_user_name) {
		this.hx_user_name = hx_user_name;
	}

	public String getHx_pwd() {
		return hx_pwd;
	}

	public void setHx_pwd(String hx_pwd) {
		this.hx_pwd = hx_pwd;
	}

    public Integer getReg_fresh() {
        return reg_fresh;
    }

    public void setReg_fresh(Integer reg_fresh) {
        this.reg_fresh = reg_fresh;
    }


    public Long getLast_login() {
        return last_login;
    }

    public void setLast_login(Long last_login) {
        this.last_login = last_login;
    }

    public Integer getDevice_type() {
        return device_type;
    }

    public void setDevice_type(Integer device_type) {
        this.device_type = device_type;
    }

    public UserExtension getExtension() {
        return extension;
    }

    public void setExtension(UserExtension extension) {
        this.extension = extension;
    }

    /**
	 * 设置当前用户和目标用户的关系
	 */
	public void setRelationShip(Integer is_follow , Integer is_leader){
		setFollow(is_follow);
		setIs_leader(is_leader);
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void protectPrivate(){
        this.setPassword(null);
        this.setMobile(null);
        this.setWeixin(null);
        this.setQq(null);
        this.setWeibo(null);
        this.setWeixin_openid(null);
        this.setEmail(null);
    }
    
}
