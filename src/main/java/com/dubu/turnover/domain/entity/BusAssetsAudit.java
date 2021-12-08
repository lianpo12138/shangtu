package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2021-04-04
 */
@Data
public class BusAssetsAudit implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer assetsId;
	private java.lang.Integer taskId;
	private java.lang.Integer agentUserId;
	private java.lang.String agentUserName;
	private java.lang.Integer receiveDeptId;
	private java.lang.String receiveDeptName;
	private java.lang.Integer managerUserId;
	private java.lang.String managerUserName;
	private java.lang.Integer useUserId;
	private java.lang.String useUserName;
	private java.lang.String assetsName;
	private java.lang.String reason;
	private java.lang.String depositAddress;
	private java.lang.String remarks;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
