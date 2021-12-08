package com.dubu.turnover.controller.erp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.SysAdminRole;
import com.dubu.turnover.domain.entity.SysDept;
import com.dubu.turnover.domain.entity.SysRole;
import com.dubu.turnover.service.SysAdminRoleService;
import com.dubu.turnover.service.SysAdminService;
import com.dubu.turnover.service.SysDeptService;
import com.dubu.turnover.service.SysRoleService;
import com.dubu.turnover.vo.DeptVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/erp/admin")
public class SysAdminsController {
	@Autowired
	private SysAdminService adminsService;
	@Resource
	private SysRoleService rolesService;
	@Resource
	private SysAdminRoleService adminRolesService;
	@Resource
   	private  SysAdminRoleService sysAdminRoleService;
	
    @Resource
	private SysDeptService sysDeptService;

	@GetMapping("detail")
	public Result detail() throws ServiceException {
		UserInfo userInfo = ThreadRequestContext.current();
		SysAdmin admin = adminsService.selectById(userInfo.getId().intValue());
		admin.setMenus(rolesService.getMenuListByUserId(admin.getId()));
		admin.setRoles(rolesService.getRolesByUserId(admin.getId()));
        List<Integer> depts=rolesService.getDeptsIdsByUserId(admin.getId());
    	List<DeptVo> deptList = new ArrayList<DeptVo>();
        for(Integer id:depts){
        	SysDept dept=sysDeptService.selectById(id);
        	DeptVo vo=new DeptVo();
        	vo.setDeptId(dept.getId());
        	vo.setDeptName(dept.getName());
        	deptList.add(vo);
        }
        admin.setDeptList(deptList);
		return ResultGenerator.success(admin);
	}
	

	@RequiresPermissions(value={"auth:user:query"})
	@GetMapping
	public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required=false) String q) {
		PageHelper.startPage(page, pageSize);
		Condition condition = new Condition(SysAdmin.class);
		Example.Criteria criteria =  condition.createCriteria();
		if(!StringUtils.isEmpty(q)){
			criteria.andLike("loginName", "%"+q+"%").orLike("userName", "%"+q+"%").orLike("phoneNo", "%"+q+"%");
		}
		condition.and().andEqualTo("delFlag", Boolean.TRUE);
		List<SysAdmin> list = adminsService.selectByCondition(condition);
		PageInfo<SysAdmin> pageInfo = new PageInfo<>(list);
		return ResultGenerator.success(pageInfo);
	}
	
	@GetMapping("roles")
	public Result adminRole(@RequestParam Integer adminId) throws ServiceException {
		Condition condition = new Condition(SysAdminRole.class);
		condition.createCriteria().andEqualTo("userId", adminId);
		List<SysAdminRole> adminRoles = adminRolesService.selectByCondition(condition);
		List<Integer> roleIdList = new ArrayList<>();
		adminRoles.forEach(c->{roleIdList.add(c.getRoleId());});
		Condition conditionx = new Condition(SysRole.class);
		if(roleIdList.size() >0){
			conditionx.createCriteria().andIn("id", roleIdList);
			List<SysRole> roles = rolesService.selectByCondition(conditionx);
			return ResultGenerator.success(roles);
		}
		return ResultGenerator.success(new ArrayList<>());
	}

	@RequiresPermissions(value={"auth:user:create","auth:user:edit"})
	@PostMapping
	public Result add(@RequestBody SysAdmin admins) {
		adminsService.save(admins);
		return ResultGenerator.success();
	}

	@RequiresPermissions(value={"auth:user:delete"})
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Integer id) {
		adminsService.deleteById(id);
		return ResultGenerator.success();
	}
	
	@GetMapping("validate")
    public Result validate(@RequestParam(required = false) Integer id, @RequestParam(required = true) String name) {
        Condition condition =  new Condition(SysAdmin.class);
        Example.Criteria  criteria = condition.createCriteria();
        if(id == null){
        	criteria.andEqualTo("loginName", name);
        }else{
        	criteria.andEqualTo("loginName", name);
        	criteria.andNotEqualTo("id", id);
        }
        List<SysAdmin> list = adminsService.selectByCondition(condition);
        return ResultGenerator.success(list.size() >0?false:true);
	}
	
	@GetMapping("all")
	public Result allAdmin() {
		return ResultGenerator.success(adminsService.selectAll());
	}
	
	@NoLogin
	@RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    public Boolean getStatus(@RequestParam(required = true) String loginName) {
		SysAdmin admin=adminsService.getAdminByLoginName(loginName);
		if(admin==null){
			   return false;
		}
        Integer count=sysAdminRoleService.getUserList(admin.getId());
        if(count>0){
        	 return true;
        }
        return false;
    }
}
