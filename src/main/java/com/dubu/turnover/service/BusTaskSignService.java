package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusTaskSignMapper;

import tk.mybatis.mapper.entity.Condition;

import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusTaskSign;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-30
 */
@Service
public class BusTaskSignService extends AbstractService<BusTaskSign> {

	@Resource
	private BusTaskSignMapper busTaskSignMapper;
	
	public void updateStatus(Integer taskId,Integer userId
			,Integer status){
		busTaskSignMapper.updateStatus(taskId, userId, status);
	}
	
	public List<BusTaskSign> getSignList(Integer taskId){
    	Condition condition = new Condition(BusTaskSign.class);
		condition.createCriteria().andEqualTo("taskId", taskId);
		List<BusTaskSign> list=busTaskSignMapper.selectByCondition(condition);
		return list;
	}
	
	public List<BusTaskSign> getOriSignList(Integer taskId){
    	Condition condition = new Condition(BusTaskSign.class);
		condition.createCriteria().andEqualTo("oriTaskId", taskId);
		List<BusTaskSign> list=busTaskSignMapper.selectByCondition(condition);
		return list;
	}

}
