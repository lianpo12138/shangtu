package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusTaskAuditService;
import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/bustaskaudit")
public class BusTaskAuditController {

    @Resource
	private BusTaskAuditService busTaskAuditService;

	@GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<BusTaskAudit> list = busTaskAuditService.selectAll();
        PageInfo<BusTaskAudit> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

	@PostMapping
    public Result add(@RequestBody BusTaskAudit busTaskAudit) {
        busTaskAuditService.save(busTaskAudit);
        return ResultGenerator.success();
	}

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        busTaskAuditService.deleteById(id);
        return ResultGenerator.success();
	}

    @PutMapping
    public Result update(@RequestBody BusTaskAudit busTaskAudit) {
        busTaskAuditService.updateById(busTaskAudit);
        return ResultGenerator.success();
	}

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        BusTaskAudit busTaskAudit = busTaskAuditService.selectById(id);
        return ResultGenerator.success(busTaskAudit);
	}
}

