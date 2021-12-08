package com.dubu.turnover.domain.entity;
import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Transient;

/*
 *  @author: smart boy
 *  @date: 2019-04-03
 */
@Data
public class UserAccounts implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer userId;
	private java.lang.String userName;
	private java.lang.String nikeName;
	private  java.lang.String headImg;
	private java.lang.String phone;
	private java.lang.String email;
	private java.lang.Integer isCompany;
	private java.math.BigDecimal totalBalance;
	//可用余额
	@Transient
	private BigDecimal balance;
	private java.math.BigDecimal freezeBalance;
	private java.lang.String password;
	private java.lang.String openId;
	private java.lang.String unionId;
	private java.lang.String qqOpenId;
	private java.lang.String channelId;
	//1:正常 2:冻结 3:关闭
	private java.lang.String status;
	private java.lang.String remarks;
	private java.lang.String isAgree;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	@Transient
	private String token;
	private String trueName;
	private String province;
	private String city;
	private String identityType;
	private String identityName;
	private Integer deptId;
}
