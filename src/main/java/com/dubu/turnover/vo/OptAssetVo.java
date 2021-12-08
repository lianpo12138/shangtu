package com.dubu.turnover.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAttachments;

@Data
public class OptAssetVo implements java.io.Serializable {
	private List<OptAssetUseVo> ids=new ArrayList<OptAssetUseVo>();

	@ApiModelProperty(value="接收部门id")
	private Integer receiveDeptId;
	@ApiModelProperty(value="接收部门名称")
	private String receiveDeptName;
	
	@ApiModelProperty(value="资产管理员id")	
	private Integer managerUserId;
	@ApiModelProperty(value="资产管理员名称")
	private String managerUserName;
	
	@ApiModelProperty(value="资产使用人id")
	private Integer useUserId;
	@ApiModelProperty(value="资产使用人名称")
	private String useUserName;
	
	@ApiModelProperty(value="存放地点")
	private java.lang.String depositAddress;
	
	@ApiModelProperty(value="原因")
	private java.lang.String reason;
	
	@ApiModelProperty(value="备注")
	private java.lang.String remarks;
	
}
