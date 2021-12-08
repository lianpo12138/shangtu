package com.dubu.turnover.mapper;

import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.BusTaskProcess;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
public interface  BusTaskAuditMapper extends Mapper<BusTaskAudit>{
	
	public List<BusTaskAudit> selectByTask(@Param("scouceId")Integer scouceId,@Param("type")Integer type);

}
