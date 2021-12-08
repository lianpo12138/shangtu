package com.dubu.turnover.service;

import com.dubu.turnover.mapper.SysDeptMapper;
import com.dubu.turnover.utils.TreeUtil;

import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusConsumeProject;
import com.dubu.turnover.domain.entity.SysDept;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-18
 */
@Service
public class SysDeptService extends AbstractService<SysDept> {

	@Resource
	private SysDeptMapper sysDeptMapper;
	
	public List<SysDept> selectAllProject(Boolean visible){
		Example con = new Example(SysDept.class);
		if(visible != null){
			con.createCriteria().andEqualTo("visible",visible);
		}
		List<SysDept> menus = sysDeptMapper.selectByCondition(con);
		return  TreeUtil.tree(menus);
	}

}
