package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusConsumeTypeService;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusConsumeType;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@RestController
@RequestMapping("/erp/busconsumetype")
@Api(value = "耗材类型管理",tags="耗材类型管理")
public class BusConsumeTypeController {

    @Resource
	private BusConsumeTypeService busConsumeTypeService;

	@GetMapping("/list")
    @ApiOperation(value = "耗材类型列表", notes = "耗材类型列表")
    public Result list(@RequestParam(required = false) String name) {
		return ResultGenerator.success(busConsumeTypeService.selectAllProject(name));
	}

	@PostMapping("/add")
    @ApiOperation(value = "耗材类型新增", notes = "耗材类型新增")
    public Result add(@RequestBody BusConsumeType busConsumeType) {
    	Condition condition = new Condition(BusConsumeType.class);
		condition.createCriteria().andEqualTo("name", busConsumeType.getName());
		Integer count  = busConsumeTypeService.selectCountByCondition(condition);
		if(count>0){
		    return ResultGenerator.error("耗材类型名称已存在,不能新增");
		}
        busConsumeTypeService.save(busConsumeType);
        return ResultGenerator.success();
	}

    @GetMapping("/del/{id}")
    @ApiOperation(value = "耗材类型删除", notes = "耗材类型删除")
    public Result delete(@PathVariable Integer id) {
        busConsumeTypeService.deleteById(id);
        return ResultGenerator.success();
	}

    @PostMapping("/update")
    @ApiOperation(value = "耗材类型修改", notes = "耗材类型修改")
    public Result update(@RequestBody BusConsumeType busConsumeType) {
        busConsumeTypeService.updateById(busConsumeType);
        return ResultGenerator.success();
	}

  
}

