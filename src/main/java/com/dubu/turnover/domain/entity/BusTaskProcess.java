package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Data
public class BusTaskProcess implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	//1固定资产入库审核2固定资产下放审核3固定资产交接审核4固定资产报废   11耗材领用一般流程12耗材领用特殊流程
	private java.lang.Integer type;
	private java.lang.String processName;
	private java.lang.String currentRoleIds;
	private java.lang.String currentDeptIds;
	private java.lang.String nextRoleIds;
	private java.lang.String nextDeptIds;
	private java.lang.Integer nextProcessId;
	private java.lang.String sendRoleIds;
	private java.lang.String sendDeptIds;
	private java.lang.String remarks;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
