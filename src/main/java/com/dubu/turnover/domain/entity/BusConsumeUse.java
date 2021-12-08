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
public class BusConsumeUse implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private java.lang.Integer id;
	@ApiModelProperty(value="库存id")
	private java.lang.Integer storeId;
	@ApiModelProperty(value="项目id")
	private java.lang.Integer projectId;
	@ApiModelProperty(value="项目名称")
	private java.lang.String projectName;
	@ApiModelProperty(value="耗材编码")
	private java.lang.String consumeNo;
	@ApiModelProperty(value="耗材名称")
	private java.lang.String consumeName;
	@ApiModelProperty(value="耗材分类id")
	private java.lang.Integer consumeCategoryId;
	private java.lang.Integer consumeCategoryName;
	@ApiModelProperty(value="耗材类型")
	private java.lang.String consumeType;
	@ApiModelProperty(value="耗材型号")
	private java.lang.String consumeModel;
	@ApiModelProperty(value="管理人id")
	private java.lang.Integer userId;
	@ApiModelProperty(value="管理人名称")
	private java.lang.String userName;
	@ApiModelProperty(value="部门id")
	private java.lang.Integer deptId;
	@ApiModelProperty(value="部门名称")
	private java.lang.String deptName;
	@ApiModelProperty(value="领用人id")
	private java.lang.Integer useUserId;
	@ApiModelProperty(value="领用人名称")
	private java.lang.String useUserName;
	@ApiModelProperty(value="领用人部门id")
	private java.lang.Integer useDeptId;
	@ApiModelProperty(value="领用人部门名称")
	private java.lang.String useDeptName;
	@ApiModelProperty(value="仓库地址id")
	private Integer storeAddressId;
	@ApiModelProperty(value="仓库地址")
	private java.lang.String storeAddress;
	@ApiModelProperty(value="仓库地址")
	private java.lang.String phone;
	@ApiModelProperty(value="标记信息")
	private java.lang.String markMessage;
	@ApiModelProperty(value="申请时间")
	private java.util.Date applyTime;
	@ApiModelProperty(value="入库时间")
	private java.util.Date inStoreTime;
	@ApiModelProperty(value="数量")
	private java.lang.Integer number;
	@ApiModelProperty(value="耗材原值")
	private java.lang.String oriConsumeName;
	@ApiModelProperty(value="1申请中2待验收3已验收4已领取5驳回")
	private java.lang.Integer status;
	@ApiModelProperty(value="用途")
	private java.lang.String purpose;
	@ApiModelProperty(value="验收人")
	private java.lang.String checkUserName;
	@ApiModelProperty(value="审批管理员")
	private java.lang.String auditUserName;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	@ApiModelProperty(value="资产编号")
	private java.lang.String assetsNo;
	@ApiModelProperty(value="资产名称")
	private java.lang.String assetsName;
	@ApiModelProperty(value="资产类型")
	private java.lang.String assetsModel;
	private String remarks;
	@Transient
	private List<BusAttachments> attList=new ArrayList<BusAttachments>();

}
