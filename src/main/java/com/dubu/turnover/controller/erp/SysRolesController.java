package com.dubu.turnover.controller.erp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.entity.SysRole;
import com.dubu.turnover.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/*
 *  @author: smart boy
 *  @date: 2018-08-06
 */
@RestController
@RequestMapping("/erp/roles")
public class SysRolesController {

    @Resource
	private SysRoleService rolesService;
 
    
    @RequiresPermissions(value={"auth:role:query"})
	@GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required = false) String roleName) {
        PageHelper.startPage(page, pageSize);
        Condition condition =  new Condition(SysRole.class);
        if(!StringUtils.isEmpty(roleName)){
        	condition.createCriteria().andLike("roleName", "%"+roleName+"%");
        }
        List<SysRole> list = rolesService.selectByCondition(condition);
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

	@GetMapping("menus")
    public Result menus(@RequestParam Integer roleId) {
        return ResultGenerator.success(rolesService.getRoleMenuIdList(roleId));
	}
	
	@RequiresPermissions(value={"auth:role:create","auth:role:edit"})
	@PostMapping
    public Result add(@RequestBody SysRole roles) {
        rolesService.saveOrderUpdateRoles(roles);
        return ResultGenerator.success();
	}

	@GetMapping("all")
    public Result allRoles(@RequestParam(required = false) String q) {
        Condition condition =  new Condition(SysRole.class);
        Example.Criteria  criteria = condition.createCriteria().andEqualTo("status", Boolean.TRUE);
        if(!StringUtils.isEmpty(q)){
        	criteria.andLike("roleName", "%"+q+"%");
        }
        List<SysRole> list = rolesService.selectByCondition(condition);
        return ResultGenerator.success(list);
	}
	
    @DeleteMapping("/{id}")
	@RequiresPermissions(value={"auth:role:delete"})
    public Result delete(@PathVariable Integer id) {
        rolesService.deleteById(id);
        return ResultGenerator.success();
	}
	
	@GetMapping("validate")
    public Result validate(@RequestParam(required = false) Integer id, @RequestParam(required = true) String name) {
        Condition condition =  new Condition(SysRole.class);
        Example.Criteria  criteria = condition.createCriteria();
        if(id == null){
        	criteria.andEqualTo("roleName", name);
        }else{
        	criteria.andEqualTo("roleName", name);
        	criteria.andNotEqualTo("id", id);
        }
        List<SysRole> list = rolesService.selectByCondition(condition);
        return ResultGenerator.success(list.size() >0?false:true);
	}
}

