package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusTaskAuditMapper;
import com.dubu.turnover.utils.TreeUtil;

import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.SysMenu;

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
public class BusTaskAuditService extends AbstractService<BusTaskAudit> {

	@Resource
	private BusTaskAuditMapper busTaskAuditMapper;
	
	public List<BusTaskAudit>  selectByAuditList(Integer taskId){
		Example con = new Example(BusTaskAudit.class);
			con.createCriteria().andEqualTo("taskId",taskId);
		List<BusTaskAudit> list = busTaskAuditMapper.selectByCondition(con);
		return  list;
	}
	
	public List<BusTaskAudit> selectByTask(Integer scouceId,Integer type){
		return busTaskAuditMapper.selectByTask(scouceId, type);
	}

}
