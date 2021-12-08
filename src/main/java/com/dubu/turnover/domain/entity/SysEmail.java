package com.dubu.turnover.domain.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 系统邮件
 * @author yehefeng
 *
 * http://www.dubuinfo.com
 * 上海笃步
 */
@Data
public class SysEmail {
	@Id
	private String id;
	//STMP服务器
	private String stmpServer;
	//POST
	private String post;
	//是否验证
	private Integer isCheck=1;
	//发送邮箱
	private String sendEmail;
	//邮箱用户
	private String stmpUser;
	//邮箱密码
	private String stmpPwd;
	//
	private Date createDate=new Date();
	//state
	private Integer state;
	
	public String getStmpServer() {
		return stmpServer;
	}

	public void setStmpServer(String stmpServer) {
		this.stmpServer = stmpServer;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	public String getStmpUser() {
		return stmpUser;
	}

	public void setStmpUser(String stmpUser) {
		this.stmpUser = stmpUser;
	}

	public String getStmpPwd() {
		return stmpPwd;
	}

	public void setStmpPwd(String stmpPwd) {
		this.stmpPwd = stmpPwd;
	}

	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}
