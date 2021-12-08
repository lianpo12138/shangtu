package com.dubu.turnover.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAttachments;

@Data
public class OptConsumeNumberVo implements java.io.Serializable {
	private Integer id;
	
	@ApiModelProperty(value="数量")
	private Integer number;	
	
}
