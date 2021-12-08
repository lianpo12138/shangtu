package com.dubu.turnover.service;

import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.domain.entity.SysMenu;
import com.dubu.turnover.mapper.SysMenuMapper;
import com.dubu.turnover.utils.TreeUtil;

import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Service
public class SysMenuService extends AbstractService<SysMenu> {
	@Resource
	private SysMenuMapper sysMenuMapper;
	public List<SysMenu> selectAllMenus(Boolean visible){
		Example con = new Example(SysMenu.class);
		if(visible != null){
			con.createCriteria().andEqualTo("visible",visible);
		}
		List<SysMenu> menus = sysMenuMapper.selectByCondition(con);
		return  TreeUtil.tree(menus);
	}
}
