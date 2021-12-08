package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusMsgTemplateService;
import com.dubu.turnover.domain.entity.BusConsumeCategory;
import com.dubu.turnover.domain.entity.BusMsgTemplate;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-04-13
 */
@RestController
@RequestMapping("/erp/busmsgtemplate")
@Api(value = "消息模板",tags="消息模板")
public class BusMsgTemplateController {

    @Resource
	private BusMsgTemplateService busMsgTemplateService;

	@GetMapping("/list")
	@ApiOperation(value = "消息模板列表", notes = "消息模板列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Condition condition =  new Condition(BusMsgTemplate.class);
        List<BusMsgTemplate> list = busMsgTemplateService.selectByCondition(condition);
        PageInfo<BusMsgTemplate> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

	@PostMapping("/add")
	@ApiOperation(value = "消息模板新增", notes = "消息模板新增")
    public Result add(@RequestBody BusMsgTemplate busMsgTemplate) {
        busMsgTemplateService.save(busMsgTemplate);
        return ResultGenerator.success();
	}

    @GetMapping("/del/{id}")
	@ApiOperation(value = "消息模板删除", notes = "消息模板删除")
    public Result delete(@PathVariable Integer id) {
        busMsgTemplateService.deleteById(id);
        return ResultGenerator.success();
	}

	@PostMapping("/update")
	@ApiOperation(value = "消息模板修改", notes = "消息模板修改")
    public Result update(@RequestBody BusMsgTemplate busMsgTemplate) {
        busMsgTemplateService.updateById(busMsgTemplate);
        return ResultGenerator.success();
	}

    @GetMapping("/detail/{id}")
	@ApiOperation(value = "消息模板详情", notes = "消息模板详情")
    public Result detail(@PathVariable Integer id) {
        BusMsgTemplate busMsgTemplate = busMsgTemplateService.selectById(id);
        return ResultGenerator.success(busMsgTemplate);
	}
}

