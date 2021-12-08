package com.dubu.turnover.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.domain.entity.SysRoleMenu;
import com.dubu.turnover.mapper.SysRoleMenuMapper;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Service
public class SysRoleMenuService extends AbstractService<SysRoleMenu> {

	@Resource
	private SysRoleMenuMapper sysRoleMenuMapper;

}
