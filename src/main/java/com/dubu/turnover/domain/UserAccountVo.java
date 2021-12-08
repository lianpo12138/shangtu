package com.dubu.turnover.domain;

import com.dubu.turnover.domain.entity.UserAccounts;

import lombok.Data;

public class UserAccountVo {

	private String userid;
	private String username;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
