package com.dubu.turnover.domain.entity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Data
public class BusConsumeStore implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private java.lang.Integer id;
	private java.lang.Integer userId;
	private java.lang.String userName;
	@ApiModelProperty(value="所属部门")
	private java.lang.Integer deptId;
	@ApiModelProperty(value="所属部门名称")
	private java.lang.String deptName;
	@ApiModelProperty(value="项目编号")
	private java.lang.Integer projectId;
	@ApiModelProperty(value="项目名称")
	private java.lang.String projectName;
	@ApiModelProperty(value="分类id")
	private java.lang.Integer categoryId;
	@ApiModelProperty(value="耗材名称")
	private java.lang.String consumeName;
	@ApiModelProperty(value="耗材型号")
	private java.lang.String consumeType;
	@ApiModelProperty(value="耗材型号")
	private java.lang.String consumeModel;
	@ApiModelProperty(value="存放地点id")
	private Integer storeAddressId;
	@ApiModelProperty(value="存放地点")
	private java.lang.String storeAddress;
	@ApiModelProperty(value="是否验收")
	private java.lang.String isAccept="0";
	private java.lang.String remarks;
	@ApiModelProperty(value="流程类型1一般流程2文献流程")
	private java.lang.Integer processType;
	
	@ApiModelProperty(value="库存数量")
	private java.lang.Integer number=0;
	
	@ApiModelProperty(value="申请领用数量")
	private java.lang.Integer applyNumber=0;
	
	@ApiModelProperty(value="出库数量")
	private java.lang.Integer outNumber=0;
	
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	@Transient
	private List<BusAttachments> attList=new ArrayList<BusAttachments>();

}
