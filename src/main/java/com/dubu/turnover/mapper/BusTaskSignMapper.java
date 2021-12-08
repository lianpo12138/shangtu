package com.dubu.turnover.mapper;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.domain.entity.BusTaskSign;
import com.dubu.turnover.core.Mapper;

/*
 *  @author: smart boy
 *  @date: 2021-03-30
 */
public interface  BusTaskSignMapper extends Mapper<BusTaskSign>{
	public void updateStatus(@Param("taskId")Integer taskId,@Param("userId")Integer userId
			,@Param("status")Integer status);
}
