package com.dubu.turnover.controller.erp;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.entity.SysFeeSetting;
import com.dubu.turnover.service.SysFeeSettingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2019-04-02
 */
@RestController
@RequestMapping("/erp/sysfeesetting")
public class SysFeeSettingController {

    @Resource
	private SysFeeSettingService sysFeeSettingService;

    @RequiresPermissions(value={"system:fee:query"})
	@GetMapping
	public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(required = false) Integer id, @RequestParam(required = false) Integer feeSubjectType,
			@RequestParam(required = false) Integer feeSubject, @RequestParam(required = false) Integer feeType) {
        PageHelper.startPage(page, size);
    	Condition condition = new Condition(SysFeeSetting.class);
		Example.Criteria criteria = condition.createCriteria();
		if (id!=null) {
			criteria.andEqualTo("id", id);
		}
		if (feeSubjectType!=null) {
			criteria.andEqualTo("feeSubjectType", feeSubjectType);
		}
		if (feeSubject!=null) {
			criteria.andEqualTo("feeSubject", feeSubject);
		}
		if (feeType!=null) {
			criteria.andEqualTo("feeType", feeType);
		}
		List<SysFeeSetting> list = sysFeeSettingService.selectByCondition(condition);
		PageInfo<SysFeeSetting> pageInfo = new PageInfo<>(list);
		return ResultGenerator.success(pageInfo);
	}
    @RequiresPermissions(value={"system:fee:update"})
    @PutMapping
    public Result update(@RequestBody SysFeeSetting sysFeeSetting) {
        sysFeeSettingService.updateById(sysFeeSetting);
        return ResultGenerator.success();
	}
    @RequiresPermissions(value={"system:fee:create"})
    @PostMapping
    public Result save(@RequestBody SysFeeSetting sysFeeSetting) {
        sysFeeSettingService.save(sysFeeSetting);
        return ResultGenerator.success();
	}
}

