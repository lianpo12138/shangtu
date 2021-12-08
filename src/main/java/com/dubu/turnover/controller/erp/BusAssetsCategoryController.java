package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAssetsCategoryService;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsCategory;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import org.springframework.web.bind.annotation.*;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-04-21
 */
@Api(value = "固定资产大类",tags="固定资产大类")
@RestController
@RequestMapping("/erp/busassetscategory")
public class BusAssetsCategoryController {

    @Resource
	private BusAssetsCategoryService busAssetsCategoryService;

    
    @ApiOperation(value = "固定资产大类查询", notes = "固定资产大类查询")
	@GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "name", value = "名称", dataType = "String"),
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
    		@RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String name) {
        PageHelper.startPage(page, size);
		Condition condition = new Condition(BusAssetsCategory.class);
		Example.Criteria criteria = condition.createCriteria();
		if (!StringUtil.isEmpty(name)) {
			criteria.andLike("name", "%"+name+"%");
		}
        List<BusAssetsCategory> list = busAssetsCategoryService.selectByCondition(condition);
        PageInfo<BusAssetsCategory> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

    @ApiOperation(value = "固定资产大类新增", notes = "固定资产大类新增")
	@PostMapping("/add")
    public Result add(@RequestBody BusAssetsCategory busAssetsCategory) {
        busAssetsCategoryService.save(busAssetsCategory);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "固定资产大类删除", notes = "固定资产大类删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        busAssetsCategoryService.deleteById(id);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "固定资产大类修改", notes = "固定资产大类修改")
    @PostMapping("/update")
    public Result update(@RequestBody BusAssetsCategory busAssetsCategory) {
        busAssetsCategoryService.updateById(busAssetsCategory);
        return ResultGenerator.success();
	}


}

