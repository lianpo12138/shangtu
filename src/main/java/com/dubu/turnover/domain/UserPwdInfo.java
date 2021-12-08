package com.dubu.turnover.domain;

import io.swagger.annotations.ApiModelProperty;

public class UserPwdInfo {
	private static final long serialVersionUID = -4386729800315338025L;

    @ApiModelProperty(value = "密码")
    private String password;  
    @ApiModelProperty(value = "电话")
    private String phone; 
    @ApiModelProperty(value = "验证码")
    private String validationCode;
    @ApiModelProperty(value = "email")
    private String email; 
    
    
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getValidationCode() {
		return validationCode;
	}
	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
