package com.dubu.turnover.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAttachments;

@Data
public class OptScrapVo implements java.io.Serializable {
	
	@ApiModelProperty(value="ids")
	private List<OptAssetUseVo> ids=new ArrayList<OptAssetUseVo>();
	
	
	@ApiModelProperty(value="状态")
	private java.lang.Integer status;
	
}
