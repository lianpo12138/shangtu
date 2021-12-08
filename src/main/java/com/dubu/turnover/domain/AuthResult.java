package com.dubu.turnover.domain;

public class AuthResult {
    private String userId;    //userId
    private String name;    //username
    private String password; //瀵嗙爜
    private String loginIp;
    private String registerIp;
    private String validationCode;
    private Integer appType; //瀹㈡埛绔被鍨�  web/iphone/android
    private String appVersion;
    private String appId;   // id
    private String appKey;  // token
    private Integer isRandomPwd;
    private String randomPwd;
    private String email;
    private String mobilePhone;
    private String nickname;
    private String ticket;     //鐢ㄦ埛淇℃伅绁ㄦ嵁銆傚鏋滆鏁板�兼洿鏂帮紝鍒欑涓夋柟闇�閲嶆柊鎷夊彇鐢ㄦ埛淇℃伅浠ヨ幏鍙栨渶鏂版暟鎹�
    private int agreement = 0; //鏄惁鍚屾剰鏈嶅姟鏉℃(0:涓嶅悓鎰� 1:鍚屾剰)
    private String channelId ; //娓犻亾鍙凤紙1:qq,2锛氬井淇�,3:寰崥锛�
    private String openId ; //绗笁鏂圭櫥褰曞悗鐨勫敮涓�鏍囪瘑

    private String userLang; //0榛樿锛�1涓浗锛�2鑻辨枃

	//鐢ㄦ埛绫诲瀷
    private Integer userType = 0;
    
    private String  uinionId;   //寰俊鐧诲綍鏃跺敮涓�id

    private String headImgUrl; //绗笁鏂圭敤鎴峰ご鍍�

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Integer getIsRandomPwd() {
		return isRandomPwd;
	}

	public void setIsRandomPwd(Integer isRandomPwd) {
		this.isRandomPwd = isRandomPwd;
	}

	public String getRandomPwd() {
		return randomPwd;
	}

	public void setRandomPwd(String randomPwd) {
		this.randomPwd = randomPwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getAgreement() {
		return agreement;
	}

	public void setAgreement(int agreement) {
		this.agreement = agreement;
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

	public String getUserLang() {
		return userLang;
	}

	public void setUserLang(String userLang) {
		this.userLang = userLang;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUinionId() {
		return uinionId;
	}

	public void setUinionId(String uinionId) {
		this.uinionId = uinionId;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
    
}
