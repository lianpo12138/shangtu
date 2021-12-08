package com.dubu.turnover.domain.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class OSInfo implements Serializable {

    private static final long serialVersionUID = -8062758991131384118L;
    /**
     * app
     */
    private String app;

    /**
     * 平台（安卓， IOS， H5，PC， 小程序）
     */
    private Platform os = Platform.H5;

    /**
     * 平台操作系统版本
     */
    private String osVersion;

    /**
     * App 版本
     */
    private String appVersion;

    /**
     * 移动设备ID
     */
    private String deviceId;

    /**
     * 极光推送绑定id
     */
    private String binderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * appID 和 authorization 一起用于安全验证
     */
    private String appId;


    /**
     * authorization 和 appID 一起用于安全验证
     */
    private String authorization;

    /**
     * ip
     */
    private String ip = "0.0.0.0";

    public OSInfo() {
    }

    /**
     * 根据 JSON 进行构造
     *
     * @param headers
     */
    public OSInfo(JSONObject headers) {
        this.setAppId(headers.getString("x-zhao-appid"));
        this.setDeviceId(headers.getString("x-zhao-deviceid"));

        String userId = headers.getString("x-zhao-userid");
        this.setUserId(StringUtils.isBlank(userId) ? null : Long.parseLong(userId));
        this.setAuthorization(headers.getString("authorization"));
        this.setBinderId(headers.getString("binderid"));
        JSONObject osInfo = JSON.parseObject(headers.getString("x-zhao-osinfo"));
        if (osInfo != null) {
            if (!StringUtils.isBlank(osInfo.getString("os")))
                this.setOs(Platform.valueOf(osInfo.getString("os").toUpperCase()));
            this.setOsVersion(osInfo.getString("osv"));
            this.setAppVersion(osInfo.getString("appVer"));
            this.setApp(osInfo.getString("app"));
        }
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBinderId() {
        return binderId;
    }

    public void setBinderId(String binderId) {
        this.binderId = binderId;
    }

    public void setOs(Platform os) {
        this.os = os;
    }

    public Platform getOs() {
        return os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIp() {
		return ip;
	}

	public OSInfo setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "OSInfo{" +
                "os=" + os +
                ", osVersion='" + osVersion + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", binderId='" + binderId + '\'' +
                ", userId=" + userId +
                ", appId='" + appId + '\'' +
                ", authorization='" + authorization + '\'' +
                '}';
    }

    public enum Platform {
        IOS, ANDROID, PC, H5, WX_XCX;
    }

}
