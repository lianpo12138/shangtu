package com.dubu.turnover.domain;


import java.io.Serializable;

/**
 * 用户基本信息
 *
 * @author ranqiulin
 */
public class LoginInfo implements Serializable {

    private static final long serialVersionUID = -6997864470587487903L;

    public LoginInfo() {
    }

    public LoginInfo(AuthResult authResult) {
        this.userId = Long.valueOf(authResult.getUserId());
        this.mobile = authResult.getMobilePhone();
        this.headImgUrl = authResult.getHeadImgUrl();
        this.ticket = authResult.getTicket();
        this.openId = authResult.getOpenId();
        this.channelId = authResult.getChannelId();

    }


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像
     */
    private String headImgUrl;

    /**
     * appId
     */
    private String id;

    /**
     * appKey
     */
    private String key;

    /**
     * ticket,用来通知第三方系统更新用户信息用的
     */
    private String ticket;

    /**
     * openId
     */
    private String openId;

    /**
     * 微信unionId
     */
    private String channelId;

    /**
     * 登录时间，格式：yyyy-MM-dd
     */
    private String loginTime;

    private String redirect;

    /**
     * 是否需要设置初始密码
     */
    private Boolean needInitPassword;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Boolean getNeedInitPassword() {
        return needInitPassword;
    }

    public void setNeedInitPassword(Boolean needInitPassword) {
        this.needInitPassword = needInitPassword;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", ticket='" + ticket + '\'' +
                ", openId='" + openId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", redirect='" + redirect + '\'' +
                ", needInitPassword=" + needInitPassword +
                '}';
    }
}
