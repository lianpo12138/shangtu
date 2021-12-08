package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusTaskProcessMapper;
import com.dubu.turnover.domain.entity.BusTaskProcess;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusTaskProcessService extends AbstractService<BusTaskProcess> {

	@Resource
	private BusTaskProcessMapper busTaskProcessMapper;
	
	public BusTaskProcess selectFirstTask(Integer type){
		return busTaskProcessMapper.selectFirstTask(type);
	}
}
