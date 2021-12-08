package com.dubu.turnover.mapper;

import com.dubu.turnover.domain.entity.BusConsumeStore;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
public interface  BusConsumeStoreMapper extends Mapper<BusConsumeStore>{
	
	public void updateApplyNumber(@Param("id")Integer id,@Param("number")Integer number);
	
	public void updateStoreNumber(@Param("id")Integer id,@Param("number")Integer number);
	
	public void updateOutStoreNumber(@Param("id")Integer id,@Param("number")Integer number);

}
