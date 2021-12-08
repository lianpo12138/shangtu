package com.dubu.turnover.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户登录注册VO（原生/第三方）
 *
 * @author ranqiulin
 */
public class UserAuthVo implements Serializable {

    private static final long serialVersionUID = 8861349529171491151L;
    @ApiModelProperty(value = "账号")
    private Integer userId; 
    @ApiModelProperty(value = "登录名称")
    private String userName; 
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "email")
    private String email;
    @ApiModelProperty(value = "验证码")
    private String validationCode;
    @ApiModelProperty(value = "昵称")
    private String nickname;// 昵称
    @ApiModelProperty(value = "(1.qq，2.微信，3.微博) 第三方类型")
    private String channelId;// (1.qq，2.微信，3.微博) 第三方类型
    private String openId;// 第三方openId
    private String unionId;// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
    private String headImgUrl;//用户头像
    private String isAgree="0";//用户头像
    
    
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }



    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }



    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
    

    public String getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}

	
}
