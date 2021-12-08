package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@Data
public class BusConsumeCategory implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	@ApiModelProperty(value="项目id")
	private java.lang.Integer projectId;
	@ApiModelProperty(value="deptid")
	private java.lang.Integer deptId;
	@ApiModelProperty(value="部门名称")
	private java.lang.String deptName;
	@ApiModelProperty(value="项目名称")
	private java.lang.String projectName;
	@ApiModelProperty(value="配件名称")
	private java.lang.String consumeName;
	@ApiModelProperty(value="配件型号")
	private java.lang.String consumeModel;
	@ApiModelProperty(value="存放地点")
	private java.lang.String depositAddress;
	@ApiModelProperty(value="耗材类型id")
	private java.lang.Integer consumeTypeId;
	@ApiModelProperty(value="耗材类型名称")
	private java.lang.String consumeTypeName;
	@ApiModelProperty(value="是否验收1是0否")
	private java.lang.Integer isAccept=0;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	private java.lang.String remark;

}
