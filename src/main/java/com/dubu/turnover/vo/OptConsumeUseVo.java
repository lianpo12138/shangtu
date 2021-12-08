package com.dubu.turnover.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAttachments;

@Data
public class OptConsumeUseVo implements java.io.Serializable {
	
	@ApiModelProperty(value="领用记录")
    private List<OptConsumeNumberVo> numList=new ArrayList<OptConsumeNumberVo>();
	
	@ApiModelProperty(value="领用人")
	private java.lang.String userName;
	
	@ApiModelProperty(value="耗材用途")
	private java.lang.String purpose;
	
	@ApiModelProperty(value="资产编号")
	private java.lang.String assetsNo;
	
	@ApiModelProperty(value="领用地址")
	private Integer useAddressId;
	
	@ApiModelProperty(value="备注")
	private java.lang.String remarks;
	
}
