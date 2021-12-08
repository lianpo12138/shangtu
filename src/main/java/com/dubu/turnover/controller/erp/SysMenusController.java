package com.dubu.turnover.controller.erp;

import org.springframework.web.bind.annotation.*;

import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.entity.SysMenu;
import com.dubu.turnover.service.SysMenuService;

import javax.annotation.Resource;

/*
 *  @author: smart boy
 *  @date: 2018-08-06
 */
@RestController
@RequestMapping("/erp/menus")
public class SysMenusController {

	@Resource
	private SysMenuService menusService;

	@RequiresPermissions(value={"auth:menus:query"})
	@GetMapping
	public Result allTree(@RequestParam(required=false) Boolean visible) {
		return ResultGenerator.success(menusService.selectAllMenus(visible));
	}

	@RequiresPermissions(value={"auth:menus:detail"})
	@GetMapping("/{id}")
	public Result get(@PathVariable(name = "id") Integer id) {
		return ResultGenerator.success(menusService.selectById(id));
	}

	@RequiresPermissions(value={"auth:menus:edit","auth:menus:create"})
	@PostMapping
	public Result add(@RequestBody SysMenu menus) {
		menusService.updateById(menus);
		return ResultGenerator.success();
	}
}
