package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusAssetsLogMapper;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.SysFeeSetting;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusAssetsLogService extends AbstractService<BusAssetsLog> {

	@Resource
	private BusAssetsLogMapper busAssetsLogMapper;
	
	public List<BusAssetsLog> getAssetsLog(Integer assetsId){
		Condition condition = new Condition(BusAssetsLog.class);
		condition.createCriteria().andEqualTo("assetsId", assetsId);
		List<BusAssetsLog> list = busAssetsLogMapper.selectByCondition(condition);
		return list;
	}

}
