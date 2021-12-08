package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAttachmentsService;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/erp/busattachments")
public class BusAttachmentsController {

    @Resource
	private BusAttachmentsService busAttachmentsService;

    
    @ApiOperation(value = "获取附件信息", notes = "获取附件信息")
	@GetMapping("/getAttachments")
    public Result list(@RequestParam(required = false) Integer sourceId, 
    		@RequestParam(required = false) Integer type) {
        List<BusAttachments> list = busAttachmentsService.selectAll();
        PageInfo<BusAttachments> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}
}

