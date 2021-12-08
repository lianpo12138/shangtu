package com.dubu.turnover.domain.entity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Data
public class BusTask implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private java.lang.Integer id;
	@ApiModelProperty(value="1固定资产2耗材资产")
	private java.lang.Integer type;
	@ApiModelProperty(value="1固定资产下放2固定资产交接3耗材资产领用")
	private java.lang.Integer taskType;
	@ApiModelProperty(value="任务名称")
	private java.lang.String taskName;
	@ApiModelProperty(value="1待审核2已通过3不通过")
	private java.lang.Integer status;
	@ApiModelProperty(value="步骤状态")
	private java.lang.Integer taskStepStatus;
	@ApiModelProperty(value="步骤名称")
	private java.lang.String taskStepName;
	@ApiModelProperty(value="分类1审核2确认")
	private Integer categoryId;
	private java.lang.Integer applyUserId;
	private java.lang.String applyUserName;
	private java.lang.Integer applyDeptId;
	private java.lang.String applyDeptName;
	private java.lang.Integer auditRoleId;
	private java.lang.String auditRoleName;
	private java.lang.Integer auditDeptId;
	private java.lang.String auditDeptName;
	private java.lang.Integer auditUserId;
	private java.lang.Integer currentProcessId;
	private java.lang.Integer nextProcessId;
	private java.lang.Integer oriTaskId;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
