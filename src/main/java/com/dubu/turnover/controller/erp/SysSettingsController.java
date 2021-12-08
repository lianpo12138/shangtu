package com.dubu.turnover.controller.erp;

import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.entity.SysSettings;
import com.dubu.turnover.service.SysSettingsService;
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
@RequestMapping("/erp/syssettings")
public class SysSettingsController {

    @Resource
	private SysSettingsService sysSettingsService;

    @RequiresPermissions(value={"system:setting:query"})
	@GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageHelper.startPage(page, size);
        List<SysSettings> list = sysSettingsService.selectAll();
        PageInfo<SysSettings> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

    @RequiresPermissions(value={"system:setting:update"})
    @PutMapping
    public Result update(@RequestBody SysSettings sysSettings) {
        sysSettingsService.updateById(sysSettings);
        return ResultGenerator.success();
	}
}

