package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Data
public class BusConsumeLog implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer consumeId;
	private java.lang.String consumeName;
	private java.lang.Integer optUserId;
	private java.lang.String optUserName;
	private java.lang.Integer optDeptId;
	private java.lang.String optDeptName;
	private java.lang.Integer auctionType;
	private java.lang.String auctionName;
	private java.lang.String remarks;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
