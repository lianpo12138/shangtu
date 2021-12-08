package com.dubu.turnover.domain.entity;
import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Transient;
/*
 *  @author: smart boy
 *  @date: 2019-04-03
 */
@Data
public class UserCodes implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.String phone;
	private java.lang.String code;
	private java.util.Date createTime;
	
}
