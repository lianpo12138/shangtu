package com.dubu.turnover.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.entity.SysAdminRole;
import com.dubu.turnover.domain.entity.SysMenu;
import com.dubu.turnover.domain.entity.SysRole;
import com.dubu.turnover.domain.entity.SysRoleMenu;
import com.dubu.turnover.mapper.SysAdminRoleMapper;
import com.dubu.turnover.mapper.SysMenuMapper;
import com.dubu.turnover.mapper.SysRoleMapper;
import com.dubu.turnover.mapper.SysRoleMenuMapper;
import com.dubu.turnover.utils.TreeUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Service
public class SysRoleService extends AbstractService<SysRole> {
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysRoleMapper rolesMapper;
	@Resource
	private SysAdminRoleMapper adminRolesMapper;
	@Resource
	private SysRoleMenuMapper roleMenusMapper;
	@Resource
	private SysMenuMapper menusMapper;
	@Resource
	private SysAdminRoleMapper adminRolesService;

	/**
	 * 查询用户权限码
	 * @param userId
	 * @return
	 */
	public Set<String> selectPermsByUserId(Integer userId){
		Set<String> permission = new HashSet<>();
		List<SysMenu> menus = this.getMenuListByUserId(userId);
		//只存储了跟结点。菜单渲染时，如果没有子结点但是无父结点，则关联查询
		List<SysMenu> newMenus = new ArrayList<>();
		menus.forEach(c->{this.component(menus, c, newMenus);});
		menus.addAll(newMenus);
		menus.stream().filter(c->c!=null).collect(Collectors.toList()).forEach(c->{
			permission.add(c.getPerms());
		});
		return permission;
	}

	/**
	 * 查询用户角色菜单列表
	 * @param roleId
	 * @return
	 */
	public Set<Integer> getRoleMenuIdList(Integer roleId){
		Example condition = new Example(SysRoleMenu.class);
		condition.createCriteria().andEqualTo("roleId", roleId);
		List<SysRoleMenu> roleMenus = roleMenusMapper.selectByCondition(condition);
		Set<Integer> menuIds = new HashSet<>();
		roleMenus.forEach(c->{
			menuIds.add(c.getMenuId());
		});
		return menuIds;
	}

	/**
	 * 查询用户菜单
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List<SysMenu> getMenusByUserId(Integer userId) throws ServiceException {
		return TreeUtil.tree(this.getMenuListByUserId(userId));
	}

	/**
	 * 查询用户角色
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List<SysRole> getRolesByUserId(Integer userId) throws ServiceException{
		SysAdminRole ar = new SysAdminRole();
		ar.setUserId(userId);
		List<SysAdminRole> adminRoles = adminRolesMapper.select(ar);
		if(adminRoles.size()<0){
			throw new ServiceException("没有权限角色");
		}
		List<SysRole> roleIdList = new ArrayList<>();
		for(SysAdminRole adminRole :adminRoles ){
			SysRole roles = rolesMapper.selectByPrimaryKey(adminRole.getRoleId());
			roleIdList.add(roles);
		}
		return roleIdList;
	}

	/**
	 * 查询用户菜单列表
	 * @param userId
	 * @return
	 */
	public List<SysMenu> getMenuListByUserId(Integer userId){
		if(userId == 1L){
			Example con = new Example(SysMenu.class);
			con.createCriteria().andEqualTo("visible",Boolean.TRUE);
			return menusMapper.selectByCondition(con);
		}
		List<SysRole> adminRoles = getRolesByUserId(userId);
		List<Integer> roleIdList = new ArrayList<>();
		adminRoles.forEach(c->{roleIdList.add(c.getId());});
		Example condition = new Example(SysRoleMenu.class);
		condition.createCriteria().andIn("roleId", roleIdList);
		List<SysRoleMenu> roleMenus = roleMenusMapper.selectByCondition(condition);
		List<Integer> menuIdList = new ArrayList<>();
		for(SysRoleMenu roleMenu :roleMenus ){
			menuIdList.add(roleMenu.getMenuId());
		}
		Example conditionx = new Example(SysMenu.class);
		conditionx.createCriteria().andIn("id", menuIdList).andEqualTo("visible", Boolean.TRUE);
		List<SysMenu> menus = menusMapper.selectByCondition(conditionx);

		//只存储了跟结点。菜单渲染时，如果没有子结点但是无父结点，则关联查询
		List<SysMenu> newMenus = new ArrayList<>();
		for(SysMenu m : menus){
			this.component(menus, m, newMenus);
		}
		menus.addAll(newMenus);

		return menus;
	}

	/**
	 * 更新用户或新增角色
	 * @param roles
	 * @throws ServiceException
	 */
	@Transactional
	public void saveOrderUpdateRoles(SysRole roles) throws ServiceException{
		if(roles.getMenuIdList().size() <1){
			throw new ServiceException("没有权限角色");
		}
		if(roles.getId() == null){
			roles.setStatus(Boolean.TRUE);
			roles.setCreateTime(DateTime.now().toDate());
			rolesMapper.insert(roles);
			List<SysRoleMenu> roleMenusList = new ArrayList<>();
			this.batchRoleMenu(roleMenusList,roles);
			roleMenusMapper.insertList(roleMenusList);
		}else{
			SysRole role = rolesMapper.selectByPrimaryKey(roles.getId());
			role.setUpdateTime(DateTime.now().toDate());
			role.setRoleName(roles.getRoleName());
			role.setRoleSort(roles.getRoleSort());
			rolesMapper.updateByPrimaryKey(role);
			Example condition = new Example(SysRoleMenu.class);
			condition.createCriteria().andEqualTo("roleId", roles.getId());
			roleMenusMapper.deleteByCondition(condition);
			List<SysRoleMenu> roleMenusList = new ArrayList<>();
			this.batchRoleMenu(roleMenusList,roles);
			roleMenusMapper.insertList(roleMenusList);
		}
	}

	/**
	 * 删除角色
	 * @param id
	 * @throws ServiceException
	 */
	public void deleteById(Integer id) throws ServiceException{
		//是否被占用
		Condition condition = new Condition(SysAdminRole.class);
		condition.createCriteria().andEqualTo("roleId", id);
		List<SysAdminRole> list = adminRolesService.selectByCondition(condition);
		if(list.size() >0){
			throw new ServiceException("没有权限角色");
		}
		rolesMapper.deleteByPrimaryKey(id);
		Example condition2 = new Example(SysRoleMenu.class);
		condition2.createCriteria().andEqualTo("roleId", id);
		roleMenusMapper.deleteByCondition(condition2);
	}

	private void batchRoleMenu(List<SysRoleMenu> roleMenusList,SysRole roles){
		for(Integer menuId : roles.getMenuIdList()){
			SysRoleMenu roleMenus = new SysRoleMenu();
			roleMenus.setRoleId(roles.getId());
			roleMenus.setMenuId(menuId);
			roleMenusList.add(roleMenus);
		}
	}

	private void component (List<SysMenu> menus,SysMenu m,List<SysMenu> newMenus ){
		if(m.getParentId() >0 && !this.haContains(menus, m.getParentId())){
			SysMenu cm = menusMapper.selectByPrimaryKey(m.getParentId());
			newMenus.add(cm);
			if(cm.getParentId() >0){
				this.component(menus, cm, newMenus);
			}
		}
	}
	private boolean haContains(List<SysMenu> menus,Integer parentId){
		for(SysMenu m : menus){
			if(parentId.intValue() == m.getId().intValue()){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 查询用户角色
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List<Integer> getRolesIdsByUserId(Integer userId){
		SysAdminRole ar = new SysAdminRole();
		ar.setUserId(userId);
		List<SysAdminRole> adminRoles = adminRolesMapper.select(ar);

		List<Integer> roleIdList = new ArrayList<>();
		for(SysAdminRole adminRole :adminRoles ){
			roleIdList.add(adminRole.getRoleId());
		}
		return roleIdList;
	}
	public List<Integer> getDeptsIdsByUserId(Integer userId){
		SysAdminRole ar = new SysAdminRole();
		ar.setUserId(userId);
		List<SysAdminRole> adminRoles = adminRolesMapper.select(ar);

		List<Integer> deptIdList = new ArrayList<>();
		for(SysAdminRole adminRole :adminRoles ){
			SysRole role=rolesMapper.selectByPrimaryKey(adminRole.getRoleId());
			deptIdList.add(role.getDeptId());
		}
		return deptIdList;
	}
	
	/**
	 * 查询用户角色
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List<SysRole>  getRolesIdsByDeptId(Integer deptId){
		Example con = new Example(SysRole.class);
		con.createCriteria().andEqualTo("deptId",deptId);
		con.createCriteria().andLike("roleName","%管理员%");
		List<SysRole> list=rolesMapper.selectByCondition(con);
	    return  list;
	}

}
