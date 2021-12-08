package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusTaskRelationMapper;

import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusTaskRelation;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusTaskRelationService extends AbstractService<BusTaskRelation> {

	@Resource
	private BusTaskRelationMapper busTaskRelationMapper;
	
	public List<BusTaskRelation> getRelationList(Integer taskId){
		Example con = new Example(BusTaskRelation.class);
		con.createCriteria().andEqualTo("taskId",taskId);
	    List<BusTaskRelation> list = busTaskRelationMapper.selectByCondition(con);
	    return  list;
	}

}
