package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusConsumeLogService;
import com.dubu.turnover.domain.entity.BusConsumeLog;
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
@RequestMapping("/busconsumelog")
public class BusConsumeLogController {

    @Resource
	private BusConsumeLogService busConsumeLogService;

	@GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<BusConsumeLog> list = busConsumeLogService.selectAll();
        PageInfo<BusConsumeLog> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

	@PostMapping
    public Result add(@RequestBody BusConsumeLog busConsumeLog) {
        busConsumeLogService.save(busConsumeLog);
        return ResultGenerator.success();
	}

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        busConsumeLogService.deleteById(id);
        return ResultGenerator.success();
	}

    @PutMapping
    public Result update(@RequestBody BusConsumeLog busConsumeLog) {
        busConsumeLogService.updateById(busConsumeLog);
        return ResultGenerator.success();
	}

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        BusConsumeLog busConsumeLog = busConsumeLogService.selectById(id);
        return ResultGenerator.success(busConsumeLog);
	}
}

