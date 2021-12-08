package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeTypeMapper;
import com.dubu.turnover.utils.TreeUtil;

import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusConsumeType;
import com.dubu.turnover.domain.entity.SysDept;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@Service
public class BusConsumeTypeService extends AbstractService<BusConsumeType> {

	@Resource
	private BusConsumeTypeMapper busConsumeTypeMapper;
	
	public List<BusConsumeType> selectAllProject(String name){
		Example con = new Example(BusConsumeType.class);
        if(!StringUtils.isEmpty(name)){
        	con.createCriteria().andLike("name", "%"+name+"%");
        }
		List<BusConsumeType> menus = busConsumeTypeMapper.selectByCondition(con);
		return  TreeUtil.tree(menus);
	}

}
