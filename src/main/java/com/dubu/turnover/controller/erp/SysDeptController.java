package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.SysDeptService;
import com.dubu.turnover.utils.TreeUtil;
import com.dubu.turnover.domain.entity.BusConsumeProject;
import com.dubu.turnover.domain.entity.SysDept;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Example;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-03-18
 */
@RestController
@RequestMapping("/erp/sysdept")
@Api(value = "部门管理",tags="部门管理")
public class SysDeptController {

    @Resource
	private SysDeptService sysDeptService;

    @ApiOperation(value = "部门列表", notes = "部门列表")
	@GetMapping("/list")
    public Result list(@RequestParam(required=false) Boolean visible) {
		return ResultGenerator.success(sysDeptService.selectAllProject(visible));
	}

    @ApiOperation(value = "部门新增", notes = "部门新增")
	@PostMapping("/add")
    public Result add(@RequestBody SysDept sysDept) {
        sysDeptService.save(sysDept);
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "部门删除", notes = "部门删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        sysDeptService.deleteById(id);
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "部门修改", notes = "部门修改")
    @PostMapping("/update")
    public Result update(@RequestBody SysDept sysDept) {
        sysDeptService.updateById(sysDept);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "部门详情", notes = "部门详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        SysDept sysDept = sysDeptService.selectById(id);
        return ResultGenerator.success(sysDept);
	}
}

