package com.dubu.turnover.mapper;

import com.dubu.turnover.domain.entity.BusTaskProcess;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
public interface  BusTaskProcessMapper extends Mapper<BusTaskProcess>{
	
	public BusTaskProcess selectFirstTask(@Param("type")Integer type);

}
