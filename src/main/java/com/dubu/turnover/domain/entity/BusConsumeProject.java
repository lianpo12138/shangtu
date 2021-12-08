package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@Data
public class BusConsumeProject implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	@ApiModelProperty(value="项目名称")
	private java.lang.String projectName;
	@ApiModelProperty(value="经费id")
	private java.lang.Integer fundId;
	@ApiModelProperty(value="经费名称")
	private java.lang.String fundName;
	@ApiModelProperty(value="管理部门id")
	private java.lang.Integer deptId;
	@ApiModelProperty(value="管理部门")
	private java.lang.String deptName;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	private java.lang.String remark;

}
