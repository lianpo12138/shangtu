package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2021-03-30
 */
@Data
public class BusTaskSign implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer taskId;
	private java.lang.Integer oriTaskId;
	private java.lang.Integer auditUserId;
	private java.lang.String auditUserName;
	private java.lang.Integer auditDeptId;
	private java.lang.String auditDeptName;
	private java.lang.Integer auditRoleId;
	private java.lang.String  auditRoleName;
	private java.lang.Integer status;
	private java.lang.String remarks;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
