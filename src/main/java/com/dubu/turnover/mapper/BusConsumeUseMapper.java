package com.dubu.turnover.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.core.Mapper;
import com.github.pagehelper.Page;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */

public interface  BusConsumeUseMapper extends Mapper<BusConsumeUse>{
	
	public Page<BusConsumeUse> selectConsumeUsePage(@Param("consumeNo")String consumeNo,@Param("consumeName")String consumeName,
			@Param("userId")Integer userId,@Param("userName")String userName,@Param("page")Page<BusConsumeUse> page);
	
	public void updateStatus(@Param("id")Integer id,@Param("status")Integer status,@Param("userId")Integer userId,
			@Param("userName")String userName);
	
	public void updateCheckUserName(@Param("id")Integer id,@Param("checkUserName")String checkUserName);
	
	public void updateAuditUserName(@Param("id")Integer id,@Param("auditUserName")String auditUserName);

}
