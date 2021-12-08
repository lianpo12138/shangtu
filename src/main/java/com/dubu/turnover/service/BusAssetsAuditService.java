package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusAssetsAuditMapper;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAssetsLog;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-04
 */
@Service
public class BusAssetsAuditService extends AbstractService<BusAssetsAudit> {

	@Resource
	private BusAssetsAuditMapper busAssetsAuditMapper;
	
	public BusAssetsAudit getAssetsAudit(Integer taskId,Integer assetsId){
		Condition condition = new Condition(BusAssetsAudit.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("assetsId", assetsId);
		criteria.andEqualTo("taskId", taskId);
		List<BusAssetsAudit> list = busAssetsAuditMapper.selectByCondition(condition);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public BusAssetsAudit getAssetsAuditByTask(Integer taskId){
		Condition condition = new Condition(BusAssetsAudit.class);
		condition.createCriteria().andEqualTo("taskId", taskId);
		List<BusAssetsAudit> list = busAssetsAuditMapper.selectByCondition(condition);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public BusAssetsAudit getAssetsAuditById(Integer assetsId){
		Condition condition = new Condition(BusAssetsAudit.class);
		condition.createCriteria().andEqualTo("assetsId", assetsId);
		condition.orderBy("id").desc();
		List<BusAssetsAudit> list = busAssetsAuditMapper.selectByCondition(condition);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<BusAssetsAudit> getAssetsAuditByList(Integer taskId){
		Condition condition = new Condition(BusAssetsAudit.class);
		condition.createCriteria().andEqualTo("taskId", taskId);
		List<BusAssetsAudit> list = busAssetsAuditMapper.selectByCondition(condition);
		return list;
	}
	
	public BusAssetsAudit getAssetsAuditByAssetsId(Integer taskId,Integer assetsId){
		Condition condition = new Condition(BusAssetsAudit.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("assetsId", assetsId);
		criteria.andEqualTo("taskId", taskId);
		List<BusAssetsAudit> list = busAssetsAuditMapper.selectByCondition(condition);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
