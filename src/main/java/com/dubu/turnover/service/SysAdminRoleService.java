package com.dubu.turnover.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.domain.entity.BusTaskRelation;
import com.dubu.turnover.domain.entity.SysAdminRole;
import com.dubu.turnover.mapper.SysAdminRoleMapper;

import tk.mybatis.mapper.entity.Example;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Service
public class SysAdminRoleService extends AbstractService<SysAdminRole> {

	@Resource
	private SysAdminRoleMapper sysAdminRoleMapper;
	
	public Integer getUserList(Integer userId){
		Example con = new Example(SysAdminRole.class);
		con.createCriteria().andEqualTo("userId",userId);
	    Integer count=sysAdminRoleMapper.selectCountByCondition(con);
	    return  count;
	}
	
	public List<SysAdminRole> getUserListByRold(Integer roleId){
		Example con = new Example(SysAdminRole.class);
		con.createCriteria().andEqualTo("roleId",roleId);
		List<SysAdminRole> list=sysAdminRoleMapper.selectByCondition(con);
	    return  list;
	}

}
