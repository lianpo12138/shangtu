package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-04-13
 */
@Data
public class BusMsgTemplate implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	@ApiModelProperty(value="模板类型")
	private java.lang.Integer type;
	@ApiModelProperty(value="模板类型名称")
	private java.lang.String typeName;
	@ApiModelProperty(value="模板标题")
	private java.lang.String title;
	@ApiModelProperty(value="审批人审核模板内容")
	private java.lang.String context;
	@ApiModelProperty(value="发起人模板内容")
	private java.lang.String sendContext;
	@ApiModelProperty(value="完成审批模板内容")
	private java.lang.String auditContext;
	@ApiModelProperty(value="确认人确认模板内容")
	private java.lang.String signContext;
	@ApiModelProperty(value="完成确认后模板内容")
	private java.lang.String finishSignContext;
	@ApiModelProperty(value="管理员审核模板内容")
	private java.lang.String checkContext;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
