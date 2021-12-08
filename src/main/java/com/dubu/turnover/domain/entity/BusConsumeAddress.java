package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2021-05-28
 */
@Data
public class BusConsumeAddress implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer deptId;
	private java.lang.String deptName;
	private java.lang.String address;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	private java.lang.String remark;

}
