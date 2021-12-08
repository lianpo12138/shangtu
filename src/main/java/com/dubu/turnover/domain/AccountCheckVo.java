package com.dubu.turnover.domain;

import com.dubu.turnover.domain.entity.UserAccounts;

public class AccountCheckVo extends UserAccounts{
	private java.math.BigDecimal inFee;
	private java.math.BigDecimal outFee;
	private java.math.BigDecimal rechangeAmount;
	private java.math.BigDecimal payAmount;
	private java.math.BigDecimal settInAmount;
	private java.math.BigDecimal settOutAmount;
	private java.math.BigDecimal remitAmount;
	private java.math.BigDecimal withsAmount;
	public java.math.BigDecimal getInFee() {
		return inFee;
	}
	public void setInFee(java.math.BigDecimal inFee) {
		this.inFee = inFee;
	}
	public java.math.BigDecimal getOutFee() {
		return outFee;
	}
	public void setOutFee(java.math.BigDecimal outFee) {
		this.outFee = outFee;
	}
	public java.math.BigDecimal getRechangeAmount() {
		return rechangeAmount;
	}
	public void setRechangeAmount(java.math.BigDecimal rechangeAmount) {
		this.rechangeAmount = rechangeAmount;
	}
	public java.math.BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(java.math.BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public java.math.BigDecimal getSettInAmount() {
		return settInAmount;
	}
	public void setSettInAmount(java.math.BigDecimal settInAmount) {
		this.settInAmount = settInAmount;
	}
	public java.math.BigDecimal getSettOutAmount() {
		return settOutAmount;
	}
	public void setSettOutAmount(java.math.BigDecimal settOutAmount) {
		this.settOutAmount = settOutAmount;
	}
	public java.math.BigDecimal getRemitAmount() {
		return remitAmount;
	}
	public void setRemitAmount(java.math.BigDecimal remitAmount) {
		this.remitAmount = remitAmount;
	}
	public java.math.BigDecimal getWithsAmount() {
		return withsAmount;
	}
	public void setWithsAmount(java.math.BigDecimal withsAmount) {
		this.withsAmount = withsAmount;
	}
	
	
}
