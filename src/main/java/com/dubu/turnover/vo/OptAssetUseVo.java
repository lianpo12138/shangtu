package com.dubu.turnover.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAttachments;

@Data
public class OptAssetUseVo implements java.io.Serializable {
	private Integer id;
	
	@ApiModelProperty(value="使用人")
	private String useUserName;	
	
	
	@ApiModelProperty(value="使用人")
	private Integer useUserId;	
	
	@ApiModelProperty(value="地址")
	private String address;	
	
}
