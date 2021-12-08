package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAttachmentsService;
import com.dubu.turnover.service.BusConsumeAddressService;
import com.dubu.turnover.service.BusConsumeCategoryService;
import com.dubu.turnover.service.BusConsumeStoreLogService;
import com.dubu.turnover.service.BusConsumeStoreService;
import com.dubu.turnover.service.SysDictionaryService;
import com.dubu.turnover.utils.ExcelUtils;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeAddress;
import com.dubu.turnover.domain.entity.BusConsumeCategory;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusConsumeStoreLog;
import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.SysDictionary;
import com.dubu.turnover.domain.entity.SysRole;
import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/erp/busconsumestore")
@Api(value = "耗材库存",tags="耗材库存")
public class BusConsumeStoreController {

    @Resource
	private BusConsumeStoreService busConsumeStoreService;
    
    @Resource
	private BusAttachmentsService busAttachmentsService;
    
    @Resource
   	private  SysDictionaryService sysDictionaryService;
    
    @Resource
	private BusConsumeCategoryService busConsumeCategoryService;
    
    @Resource
	private BusConsumeStoreLogService busConsumeStoreLogService;
    
    @Resource
	private BusConsumeAddressService busConsumeAddressService;
    
    
    @Autowired
    SysConfig sysConfig;
    

    

    @ApiOperation(value = "耗材入库列表", notes = "耗材入库列表")
	@GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String")
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String consumeName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {
		UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition =  new Condition(BusConsumeCategory.class);
		Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(consumeName)){
        	criteria.andLike("consumeName", "%"+consumeName+"%");
        }
		if(!userInfo.getDeptIdList().contains(100)){
			criteria.andIn("deptId", userInfo.getDeptIdList());
		}
		if(!StringUtil.isEmpty(orderBy)){
			if("asc".equals(asc)){
				condition.orderBy(orderBy).asc();	
			}else{
				condition.orderBy(orderBy).desc();	
			}
		}else{
			condition.orderBy("createTime").desc();	
		}
        List<BusConsumeCategory> list = busConsumeCategoryService.selectByCondition(condition);
        PageInfo<BusConsumeCategory> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

    @ApiOperation(value = "耗材新增入库", notes = "耗材新增入库")
	@PostMapping("/add")
    public Result add(@RequestBody BusConsumeStore busConsumeStore) {
    	UserInfo userInfo = ThreadRequestContext.current();
        busConsumeStoreService.saveConsumeStore(busConsumeStore,userInfo);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材库存详情", notes = "耗材库存详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
    	Map<String,Object> map=new HashMap<String,Object>();
        BusConsumeStore busConsumeStore = busConsumeStoreService.selectById(id);
        List<BusAttachments> attList= busAttachmentsService.getAttachmentsList(id, 2);
        Condition condition =  new Condition(BusConsumeStoreLog.class);
        condition.createCriteria().andEqualTo("categoryId", busConsumeStore.getCategoryId())
        .andEqualTo("addressId", busConsumeStore.getStoreAddressId());
        List<BusConsumeStoreLog> storeLog=busConsumeStoreLogService.selectByCondition(condition);
        map.put("consumeStore", busConsumeStore);
        map.put("attList", attList);
        map.put("storeLogList", storeLog);
        return ResultGenerator.success(map);
	}
    
    
    @ApiOperation(value = "耗材库存获取需要设置的附件", notes = "耗材库获取需要设置的附件")
	@GetMapping("/getAttachments")
    public Result getAttachments() {
    	Condition condition = new Condition(SysDictionary.class);
		Example.Criteria criteria = condition.createCriteria();
			criteria.andEqualTo("dictType", "CONSUMESTORE");	
		List<SysDictionary> list = sysDictionaryService.selectByCondition(condition);
        return ResultGenerator.success(list);
	}
    
    
    @ApiOperation(value = "耗材库存查询", notes = "耗材库存查询")
	@GetMapping("/storeList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String")
    })
    public Result storeList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String consumeName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {
		UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition =  new Condition(BusConsumeStore.class);
		Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(consumeName)){
        	criteria.andLike("consumeName", "%"+consumeName+"%");
        }
		if(!userInfo.getDeptIdList().contains(100)){
			criteria.andIn("deptId", userInfo.getDeptIdList());
		}
		if(!StringUtil.isEmpty(orderBy)){
			if("asc".equals(asc)){
				condition.orderBy(orderBy).asc();	
			}else{
				condition.orderBy(orderBy).desc();	
			}
		}else{
			condition.orderBy("createTime").desc();	
		}
        List<BusConsumeStore> list = busConsumeStoreService.selectByCondition(condition);
        PageInfo<BusConsumeStore> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}
    
    @ApiOperation(value = "获取库存存放地点", notes = "获取库存存放地点")
	@GetMapping("/getStoreAddress")
    public Result getStoreAddress() {
		UserInfo userInfo = ThreadRequestContext.current();
        Condition condition =  new Condition(BusConsumeAddress.class);
    		if(!userInfo.getDeptIdList().contains(100)){
            	condition.createCriteria().andIn("deptId", userInfo.getDeptIdList());
    		}	
        	//condition.createCriteria().andIn("useDeptId", userInfo.getDeptIdList());
		condition.orderBy("createTime").desc();
        List<BusConsumeAddress> list = busConsumeAddressService.selectByCondition(condition);
        return ResultGenerator.success(list);
	}

    
    @ApiOperation(value = "耗材库存导出", notes = "耗材库存导出")
   	@GetMapping("/export")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String")
    })
    public Result export(
    		@RequestParam(required = false) String consumeName) throws IOException {
   		    UserInfo userInfo = ThreadRequestContext.current();
   		    PageHelper.startPage(1, 10000);
   		 Condition condition =  new Condition(BusConsumeStore.class);
   		 Example.Criteria criteria = condition.createCriteria();
         if(!StringUtils.isEmpty(consumeName)){
        	 criteria.andLike("consumeName", "%"+consumeName+"%");
         }
 		 if(!userInfo.getDeptIdList().contains(100)){
 			criteria.andIn("deptId", userInfo.getDeptIdList());
 		 }
 		condition.orderBy("createTime").desc();	
        List<BusConsumeStore> list = busConsumeStoreService.selectByCondition(condition);
   		   if (list.size() > 0) {
   			  ExcelUtils.ExcelExporter excelExporter = ExcelUtils.newExcelExport(BusConsumeCategory.class, listExcelMap,
   					Integer.MAX_VALUE);
   			  list.forEach(excelExporter::write);
   			  return ResultGenerator.success(excelExporter.ossExcelFile(sysConfig.getShowUrl(),sysConfig.getFileUrl(), "耗材库存报表.xls"));
   		   } else {
   			return null;
   		  }
   	}
       
    private static LinkedHashMap<String, String> listExcelMap = new LinkedHashMap<String, String>() {
		{
			put("projectName", "项目名称");
			put("deptName", "部门名称");
			put("consumeName", "耗材名称");
			put("consumeModel", "耗材型号");
			put("consumeType", "耗材类型");
			put("isAccept", "是否需要验收");
			put("number", "库存数量");
			put("applyNumber", "冻结数量");
			put("applyNumber", "出库数量");
			put("createTime", "录入时间");
		}
	};
}

