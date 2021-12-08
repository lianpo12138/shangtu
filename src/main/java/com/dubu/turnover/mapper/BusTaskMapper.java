package com.dubu.turnover.mapper;

import com.dubu.turnover.domain.entity.BusTask;
import com.github.pagehelper.Page;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
public interface  BusTaskMapper extends Mapper<BusTask>{
	
	public Page<BusTask> selectYesTaskTypePage(@Param("type")Integer type,@Param("status")String status,@Param("startDate")Date startDate,
			@Param("endDate")Date endDate,@Param("page")Page<BusTask> page,@Param("auditDeptId")String auditDeptId
			,@Param("auditRoleIds")String auditRoleIds,@Param("userId")Integer userId);
	
	public Page<BusTask> selectNoTaskTypePage(@Param("type")Integer type,@Param("status")String status,@Param("startDate")Date startDate,
			@Param("endDate")Date endDate,@Param("page")Page<BusTask> page,@Param("auditDeptId")String auditDeptId
			,@Param("auditRoleIds")String auditRoleIds,@Param("userId")Integer userId);
	
	public void updateStatus(@Param("taskId")Integer taskId,@Param("status")Integer status,@Param("remarks")String remarks);
	
	public Page<BusTask> selectYesSignTypePage(@Param("type")Integer type,@Param("status")String status,@Param("startDate")Date startDate,
			@Param("endDate")Date endDate,@Param("page")Page<BusTask> page,@Param("auditDeptId")String auditDeptId
			,@Param("auditRoleIds")String auditRoleIds,@Param("userId")Integer userId);
	
	public Page<BusTask> selectSendTypePage(@Param("type")Integer type,@Param("status")String status,@Param("startDate")Date startDate,
			@Param("endDate")Date endDate,@Param("page")Page<BusTask> page,@Param("auditDeptId")String auditDeptId
			,@Param("auditRoleIds")String auditRoleIds,@Param("userId")Integer userId);
	
	public void updateRole(@Param("taskId")Integer taskId);
	
	public void updateUser(@Param("taskId")Integer taskId);
	
	public Integer  selectNoTaskCount(@Param("taskId")Integer taskId
			,@Param("auditRoleIds")String auditRoleIds,@Param("userId")Integer userId);

}
