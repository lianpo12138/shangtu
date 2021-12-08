package com.dubu.turnover.vo;

import io.swagger.annotations.ApiModelProperty;

public class ApplyVo {
	

	private java.lang.Integer id;
	
	@ApiModelProperty(value="净值")
	private java.lang.String netWorth;
	@ApiModelProperty(value="减值准备")
	private java.lang.String decrease;
	@ApiModelProperty(value="役龄预龄")
	private java.lang.String expireYear;
	public java.lang.String getNetWorth() {
		return netWorth;
	}
	public void setNetWorth(java.lang.String netWorth) {
		this.netWorth = netWorth;
	}
	public java.lang.String getDecrease() {
		return decrease;
	}
	public void setDecrease(java.lang.String decrease) {
		this.decrease = decrease;
	}
	public java.lang.String getExpireYear() {
		return expireYear;
	}
	public void setExpireYear(java.lang.String expireYear) {
		this.expireYear = expireYear;
	}
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	

}
