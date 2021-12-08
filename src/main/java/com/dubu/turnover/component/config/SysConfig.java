package com.dubu.turnover.component.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * appId 配置
 */
@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "config.sys")
public class SysConfig {
    private String defaultAppId;
    private String defaultSecret;
    private String wxToken;
    private String fileUrl;
    private String showUrl;
    private String smsUrl;
    private String backUrl;
    private String host;
    private String sendMail;
    private String userName;
    private String password;
    private String port;
	public String getDefaultAppId() {
		return defaultAppId;
	}
	public void setDefaultAppId(String defaultAppId) {
		this.defaultAppId = defaultAppId;
	}

	public String getDefaultSecret() {
		return defaultSecret;
	}
	public void setDefaultSecret(String defaultSecret) {
		this.defaultSecret = defaultSecret;
	}
	public String getWxToken() {
		return wxToken;
	}
	public void setWxToken(String wxToken) {
		this.wxToken = wxToken;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getSmsUrl() {
		return smsUrl;
	}
	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getShowUrl() {
		return showUrl;
	}
	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getSendMail() {
		return sendMail;
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
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
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
    
}
