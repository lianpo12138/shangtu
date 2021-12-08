package com.dubu.turnover.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.SysAdminRole;
import com.dubu.turnover.mapper.SysAdminMapper;
import com.dubu.turnover.utils.MD5Util;

import tk.mybatis.mapper.entity.Condition;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Service
public class SysAdminService extends AbstractService<SysAdmin> {

	@Resource
	private SysAdminMapper sysAdminMapper;
	@Resource
	private SysAdminMapper adminsMapper;
	@Resource
	private SysAdminRoleService adminRolesService;

	public SysAdmin getAdminByLoginNameAndPassword(String loginName,String password) {
		SysAdmin admin = new SysAdmin();
		admin.setLoginName(loginName);
		admin.setDelFlag(Boolean.TRUE);
		return adminsMapper.selectOne(admin);
	}

	public void updateAdmin(SysAdmin admin) {
		adminsMapper.updateByPrimaryKeySelective(admin);
	}

	@Transactional
	public void deleteById(Integer id) {
		SysAdmin admin = adminsMapper.selectByPrimaryKey(id);
		SysAdmin updateAdmin = new SysAdmin();
		updateAdmin.setId(id);
		updateAdmin.setDelFlag(Boolean.FALSE);
		adminsMapper.updateByPrimaryKeySelective(updateAdmin);
	}

	@Transactional
	public void save(SysAdmin admin) {
		if (admin.getId() == null) {
			admin.setSalt(UUID.randomUUID().toString());
			admin.setDelFlag(Boolean.TRUE);
			admin.setPassword(MD5Util.MD5Encode(
					admin.getLoginName() + admin.getPassword()
							+ admin.getSalt(), "UTF-8").toString());
			adminsMapper.insert(admin);
			if(admin.getRoleIdList().size()>0){
				List<SysAdminRole> insertList = new ArrayList<>();
				this.component(insertList,admin);
				adminRolesService.save(insertList);	
			}
		} else {
			SysAdmin adminx = adminsMapper.selectByPrimaryKey(admin.getId());
			admin.setPassword(MD5Util.MD5Encode(
					admin.getLoginName() + admin.getPassword()
							+ adminx.getSalt(), "UTF-8").toString());
			adminsMapper.updateByPrimaryKey(admin);
			Condition condition = new Condition(SysAdminRole.class);
			condition.createCriteria().andEqualTo("userId", admin.getId());
			List<SysAdminRole> list = adminRolesService
					.selectByCondition(condition);

			StringBuffer sb = new StringBuffer();
			list.forEach(c -> {
				sb.append(c.getId()).append(",");
			});

			if (sb.toString().length() > 0) {
				adminRolesService.deleteByIds(sb.substring(0,
						sb.lastIndexOf(",")));
			}
			if(admin.getRoleIdList().size()>0){
				List<SysAdminRole> insertList = new ArrayList<>();
				this.component(insertList,admin);	
				adminRolesService.save(insertList);
			}
		}
	}

	private void component(List<SysAdminRole> insertList,SysAdmin admin){
		for(Integer roleId : admin.getRoleIdList()){
			SysAdminRole adminRole = new SysAdminRole();
			adminRole.setUserId(admin.getId());
			adminRole.setRoleId(roleId);
			insertList.add(adminRole);
		}
	}
	
	public List<SysAdmin> getAdminList(Integer deptId){
		return sysAdminMapper.getAdminList(deptId);
	}
	
	public List<SysAdmin> getAdminByDept(Integer deptId,String userName){
		return sysAdminMapper.getAdminByDept(deptId,userName);
	}
	
	public SysAdmin getAdminByLoginName(String loginName) {
		SysAdmin admin = new SysAdmin();
		admin.setLoginName(loginName);
		return adminsMapper.selectOne(admin);
	}
	
	public List<SysAdmin> getAdminByRole(Integer roleId){
		return sysAdminMapper.getAdminByRole(roleId);
	}
	
}
