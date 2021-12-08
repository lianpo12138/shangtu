package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2019-04-03
 */
@Data
public class SysFeeSetting implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer feeSubjectType;
	private java.lang.Integer feeSubject;
	private java.lang.Integer feeType;
	private java.math.BigDecimal feeValue;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.Integer status;
	private java.lang.String remark;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
