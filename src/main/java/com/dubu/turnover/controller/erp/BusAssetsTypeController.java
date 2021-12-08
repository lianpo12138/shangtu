package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAssetsTypeService;
import com.dubu.turnover.service.BusConsumeTypeService;
import com.dubu.turnover.domain.entity.BusAssetsType;
import com.dubu.turnover.domain.entity.BusConsumeType;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@RestController
@RequestMapping("/erp/busassetstype")
@Api(value = "固定资产分类管理",tags="固定资产分类管理")
public class BusAssetsTypeController {

    @Resource
	private BusAssetsTypeService busAssetsTypeService;

	@GetMapping("/list")
    @ApiOperation(value = "固定资产分类列表", notes = "固定资产分类列表")
    public Result list(@RequestParam(required = false) Integer assetsCategoryId) {
		return ResultGenerator.success(busAssetsTypeService.selectAllProject(assetsCategoryId));
	}

	@PostMapping("/add")
    @ApiOperation(value = "固定资产分类新增", notes = "固定资产分类新增")
    public Result add(@RequestBody BusAssetsType busAssetsType) {
		busAssetsTypeService.save(busAssetsType);
        return ResultGenerator.success();
	}

    @GetMapping("/del/{id}")
    @ApiOperation(value = "固定资产分类删除", notes = "固定资产分类删除")
    public Result delete(@PathVariable Integer id) {
    	busAssetsTypeService.deleteById(id);
        return ResultGenerator.success();
	}

    @PostMapping("/update")
    @ApiOperation(value = "固定资产分类型修改", notes = "固定资产分类修改")
    public Result update(@RequestBody BusAssetsType busAssetsType) {
    	busAssetsTypeService.updateById(busAssetsType);
        return ResultGenerator.success();
	}

  
}

