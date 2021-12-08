package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusConsumeAddressService;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusConsumeAddress;
import com.dubu.turnover.domain.entity.BusConsumeProject;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import tk.mybatis.mapper.entity.Condition;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-05-28
 */
@RestController
@RequestMapping("/erp/busconsumeaddress")
public class BusConsumeAddressController {

    @Resource
	private BusConsumeAddressService busConsumeAddressService;

    @ApiOperation(value = "耗材地址列表", notes = "耗材地址列表")
	@GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "deptId", value = "部门id", dataType = "Integer")
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page,
    		@RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) Integer deptId) {      
		UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition =  new Condition(BusConsumeAddress.class);
        if(deptId!=null){
        	condition.createCriteria().andEqualTo("deptId",deptId);
        }else{
    		if(!userInfo.getDeptIdList().contains(100)){
    			condition.createCriteria().andIn("deptId", userInfo.getDeptIdList());
    		}

        }
		condition.orderBy("createTime").desc();
        List<BusConsumeAddress> list = busConsumeAddressService.selectByCondition(condition);
        PageInfo<BusConsumeAddress> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

    @ApiOperation(value = "耗材地址添加", notes = "耗材地址添加")
	@PostMapping("/add")
    public Result add(@RequestBody BusConsumeAddress busConsumeAddress) {
        busConsumeAddressService.save(busConsumeAddress);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材地址删除", notes = "耗材地址删除")
    @GetMapping("/detele/{id}")
    public Result delete(@PathVariable Integer id) {
        busConsumeAddressService.deleteById(id);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材地址修改", notes = "耗材地址修改")
	@PostMapping("/update")
    public Result update(@RequestBody BusConsumeAddress busConsumeAddress) {
        busConsumeAddressService.updateById(busConsumeAddress);
        return ResultGenerator.success();
	}

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        BusConsumeAddress busConsumeAddress = busConsumeAddressService.selectById(id);
        return ResultGenerator.success(busConsumeAddress);
	}
}

