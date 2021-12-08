package com.dubu.turnover.controller.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.aspect.AuthConfig;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsCategory;
import com.dubu.turnover.domain.entity.BusConsumeProject;
import com.dubu.turnover.domain.entity.SysDictionary;
import com.dubu.turnover.domain.enums.AuctionEnum;
import com.dubu.turnover.service.BusAssetsCategoryService;
import com.dubu.turnover.service.BusAssetsTypeService;
import com.dubu.turnover.service.BusConsumeCategoryService;
import com.dubu.turnover.service.BusConsumeProjectService;
import com.dubu.turnover.service.BusConsumeTypeService;
import com.dubu.turnover.service.SysAdminService;
import com.dubu.turnover.service.SysDeptService;
import com.dubu.turnover.service.SysDictionaryService;
import com.dubu.turnover.utils.JsonUtils;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

@RestController
@RequestMapping(value = "/common")
@Api(value = "公共接口",tags="公共接口")
public class CommonController {
	
	
	@Autowired
	private RedisClient redisClientUtils;
	
	@Autowired
	private AuthConfig authConfig;

	@Autowired
	private SysDeptService sysDeptService;
	
	@Autowired
	private BusConsumeProjectService busConsumeProjectService;
    
	@Autowired
	private BusConsumeTypeService busConsumeTypeService;
	
	@Autowired
	private SysAdminService adminsService;
	
	@Autowired
   	private  SysDictionaryService sysDictionaryService;
	
	@Autowired
   	private  BusAssetsTypeService busAssetsTypeService;
	
	@Autowired
	private BusAssetsCategoryService busAssetsCategoryService;
	
	
	
	
	@Value("classpath:static/bank.json")  
	private Resource bankFile;
	
	@Value("classpath:static/funds.json")  
	private Resource fundsFile;
	 
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/banks",method = RequestMethod.GET)
	@ResponseBody
	public Result queryAllBanks() throws Exception {
		List<Map<String,String>> bankList = redisClientUtils.get(Configurer.PRI_COUNTRY_BANK_KEY);
		if(bankList==null){
			bankList = JsonUtils.fromJson(IOUtils.toString(bankFile.getInputStream(),"UTF-8"), List.class, Map.class);
		}
		return ResultGenerator.success(bankList);
	}
	
	@SuppressWarnings("unchecked")
    @ApiOperation(value = "获取财务经费树", notes = "获取财务经费树")
	@RequestMapping(value="/funds",method = RequestMethod.GET)
	@ResponseBody
	public Result queryFunds() throws Exception {
		redisClientUtils.delete(Configurer.PRI_COUNTRY_FUND_KEY);
		List<Map<String,String>> bankList = redisClientUtils.get(Configurer.PRI_COUNTRY_FUND_KEY);
		if(bankList==null){
			bankList = JsonUtils.fromJson(IOUtils.toString(fundsFile.getInputStream(),"UTF-8"), List.class, Map.class);
		}
		return ResultGenerator.success(bankList);
	}
	
	
	
    @ApiOperation(value = "获取部门树", notes = "获取部门树")
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getDeptTrees",method = RequestMethod.GET)
	@ResponseBody
	public Result getDeptTrees() throws Exception {
		return ResultGenerator.success(sysDeptService.selectAllProject(null));
	}
    
    
    @ApiOperation(value = "获取人员列表", notes = "获取人员列表")
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getUserList",method = RequestMethod.GET)
	@ResponseBody
	public Result getUserList(@RequestParam(required = false)Integer deptId,
			@RequestParam(required = false)String userName) throws Exception {
    	if(deptId!=null){
    		return ResultGenerator.success(adminsService.getAdminByDept(deptId,userName));	
    	}else{
    		return ResultGenerator.success(adminsService.selectAll());	
    	}
	}
    
    @ApiOperation(value = "获取耗材类型树", notes = "获取耗材类型树")
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getCoumsumeTypeTrees",method = RequestMethod.GET)
	@ResponseBody
	public Result getCoumsumeTypeTrees() throws Exception {
		return ResultGenerator.success(busConsumeTypeService.selectAllProject(null));
	}
    
    
    
    @ApiOperation(value = "获取码值列表", notes = "获取码值列表")
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getDictCode",method = RequestMethod.GET)
	@ResponseBody
	public Result getDictCode(@RequestParam(required = true)String dictType) throws Exception {
    	Condition condition = new Condition(SysDictionary.class);
    	Example.Criteria criteria = condition.createCriteria();
    		criteria.andEqualTo("dictType", dictType);	
    	List<SysDictionary> list = sysDictionaryService.selectByCondition(condition);
        return ResultGenerator.success(list);
	} 
    
    
    @ApiOperation(value = "获取资产大类列表", notes = "获取资产大类列表")
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getAssetsCategory",method = RequestMethod.GET)
	@ResponseBody
	public Result getAssetsCategory() throws Exception {
    	List<BusAssetsCategory> list = busAssetsCategoryService.selectAll();
        return ResultGenerator.success(list);
	} 
    
    
    @ApiOperation(value = "根据资产大类获取资产分类树", notes = "根据资产大类获取资产分类树")
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getAssetsTypeTrees",method = RequestMethod.GET)
	@ResponseBody
	public Result getAssetsTypeTrees(@RequestParam(required = true)Integer categoryId) throws Exception {
		return ResultGenerator.success(busAssetsTypeService.selectAllProject(categoryId));
	}
	
}