package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAssetsAuditService;
import com.dubu.turnover.service.BusAssetsLogService;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.service.BusAttachmentsService;
import com.dubu.turnover.service.BusTaskAuditService;
import com.dubu.turnover.service.BusTaskProcessService;
import com.dubu.turnover.service.SysDeptService;
import com.dubu.turnover.service.SysDictionaryService;
import com.dubu.turnover.vo.AddressVo;
import com.dubu.turnover.vo.ApplyVo;
import com.dubu.turnover.vo.OptAssetUseVo;
import com.dubu.turnover.vo.OptAssetVo;
import com.dubu.turnover.vo.OptScrapVo;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeStoreLog;
import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.BusTaskProcess;
import com.dubu.turnover.domain.entity.SysDept;
import com.dubu.turnover.domain.entity.SysDictionary;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.redis.RedisClient;
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

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/erp/busassets")
@Api(value = "????????????",tags="????????????")
public class BusAssetsController {

    @Resource
	private BusAssetsService busAssetsService;
    @Resource
	private SysDeptService sysDeptService;
    @Resource
	private BusAssetsLogService busAssetsLogService;
    @Resource
	private BusTaskProcessService busTaskProcessService;
    
    @Resource
   	private  SysDictionaryService sysDictionaryService;
    
    @Resource
	private BusAttachmentsService busAttachmentsService;
    
    @Resource
	private BusTaskAuditService busTaskAuditService;
    
    @Autowired
    private RedisClient redisClient;
    
    @Resource
	private BusAssetsAuditService busAssetsAuditService;
    
	@InitBinder
    public void initDateFormate(WebDataBinder dataBinder) {
        dataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsNo", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsBelongId", value = "????????????", dataType = "Integer"),
        @ApiImplicitParam(name = "useDeptName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "createStartDate", value = "??????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "createEndDate", value = "??????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "financeStatus", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "cardId", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "oriAssetsNo", value = "??????????????????", dataType = "String"),
        @ApiImplicitParam(name = "bookAssetsNo", value = "??????????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsCategoryId", value = "????????????", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsDetailCategoryId", value = "????????????", dataType = "Integer"),
        @ApiImplicitParam(name = "buyStartDate", value = "??????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "buyEndDate", value = "??????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "financeStartDate", value = "????????????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "financeEndDate", value = "????????????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "number", value = "??????", dataType = "Integer"),
        @ApiImplicitParam(name = "useWay", value = "??????", dataType = "String"),
        @ApiImplicitParam(name = "deptName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "userName", value = "???????????????", dataType = "String"),
        @ApiImplicitParam(name = "useStatus", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "cardAddress", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "useUserName", value = "???????????????", dataType = "String"),
        @ApiImplicitParam(name = "useStartDate", value = "??????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "useEndDate", value = "??????????????????", dataType = "Date"),
        @ApiImplicitParam(name = "remarks", value = "??????", dataType = "String"),
        @ApiImplicitParam(name = "depositAddress", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsModel", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "????????????:asc???desc", dataType = "String"),
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String assetsNo, @RequestParam(required = false) String assetsName,
    		@RequestParam(required = false) Integer assetsBelongId,
    		@RequestParam(required = false) String useDeptName,
    		@RequestParam(required = false) Date createStartDate,
    		@RequestParam(required = false) Date createEndDate,
    		@RequestParam(required = false) String financeStatus,
    		@RequestParam(required = false) String cardId,
    		@RequestParam(required = false) String oriAssetsNo,
    		@RequestParam(required = false) String bookAssetsNo,
    		@RequestParam(required = false) Integer assetsCategoryId,
    		@RequestParam(required = false) Integer assetsDetailCategoryId,
    		@RequestParam(required = false) Date buyStartDate,
    		@RequestParam(required = false) Date buyEndDate,
    		@RequestParam(required = false) Date financeStartDate,
    		@RequestParam(required = false) Date financeEndDate,
    		@RequestParam(required = false) Integer number,
    		@RequestParam(required = false) Integer status,
      		@RequestParam(required = false) String useWay,
      		@RequestParam(required = false) String deptName,
      		@RequestParam(required = false) String userName,
      		@RequestParam(required = false) String useStatus,
      		@RequestParam(required = false) String cardAddress,
      		@RequestParam(required = false) String useUserName,
      		@RequestParam(required = false) Date useStartDate,
      		@RequestParam(required = false) Date useEndDate,
      		@RequestParam(required = false) String remarks,
      		@RequestParam(required = false) String  depositAddress,
      		@RequestParam(required = false) String  assetsModel,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc
    		) {
    	try{
		UserInfo userInfo = ThreadRequestContext.current();
		PageHelper.startPage(page, size);
		Condition condition = new Condition(BusAssets.class);
		Example.Criteria criteria = condition.createCriteria();
		if (!StringUtil.isEmpty(assetsNo)) {
			criteria.andLike("assetsNo", "%"+assetsNo+"%");
		}
		if (!StringUtil.isEmpty(assetsName)) {
			criteria.andLike("assetsName", "%"+assetsName+"%");
		}
		if (assetsBelongId!=null) {
			criteria.andEqualTo("assetsBelongId", assetsBelongId);
		}
		if(StringUtil.isEmpty(useDeptName)){
			if(!userInfo.getDeptIdList().contains(100)){
				criteria.andIn("useDeptId", userInfo.getDeptIdList());
			}
		}else{
			criteria.andEqualTo("useDeptName", useDeptName);
		}
		if (assetsBelongId!=null) {
			criteria.andEqualTo("assetsBelongId", assetsBelongId);
		}
		if(createStartDate!=null){
			criteria.andGreaterThan("createDate",createStartDate);
		}
		if(createEndDate!=null){
			criteria.andLessThan("createDate",createEndDate);
		}
		if (!StringUtil.isEmpty(financeStatus)) {
			criteria.andEqualTo("financeStatus", financeStatus);
		}
		if (!StringUtil.isEmpty(cardId)) {
			criteria.andLike("cardId", "%"+cardId+"%");
		}
		if (!StringUtil.isEmpty(oriAssetsNo)) {
			criteria.andLike("oriAssetsNo", "%"+oriAssetsNo+"%");
		}
		if (!StringUtil.isEmpty(bookAssetsNo)) {
			criteria.andLike("bookAssetsNo", "%"+bookAssetsNo+"%");
		}
		if (assetsCategoryId!=null) {
			criteria.andEqualTo("assetsCategoryId", assetsCategoryId);
		}
		if (assetsDetailCategoryId!=null) {
			criteria.andEqualTo("assetsDetailCategoryId", assetsDetailCategoryId);
		}
		if(buyStartDate!=null){
			criteria.andGreaterThanOrEqualTo("buyDate",buyStartDate);
		}
		if(buyEndDate!=null){
			criteria.andLessThanOrEqualTo("buyDate",buyEndDate);
		}
		if(financeStartDate!=null){
			criteria.andGreaterThanOrEqualTo("financeDate",financeStartDate);
		}
		if(financeEndDate!=null){
			criteria.andLessThanOrEqualTo("financeDate",financeEndDate);
		}
		if (number!=null) {
			criteria.andEqualTo("number", number);
		}
		if (!StringUtil.isEmpty(useWay)) {
			criteria.andLike("useWay", "%"+useWay+"%");
		}
		if (status!=null) {
			criteria.andEqualTo("status", status);
		}
		if (!StringUtil.isEmpty(deptName)) {
			criteria.andEqualTo("deptName", deptName);
		}
		if (!StringUtil.isEmpty(userName)) {
			criteria.andLike("userName", "%"+userName+"%");
		}
		if (!StringUtil.isEmpty(useStatus)) {
			criteria.andEqualTo("useStatus", useStatus);
		}
		if (!StringUtil.isEmpty(cardAddress)) {
			criteria.andLike("cardAddress", "%"+cardAddress+"%");
		}
		if (!StringUtil.isEmpty(useUserName)) {
			criteria.andLike("useUserName", "%"+useUserName+"%");
		}
		if (!StringUtil.isEmpty(depositAddress)) {
			criteria.andLike("depositAddress", "%"+depositAddress+"%");
		}
		if (!StringUtil.isEmpty(assetsModel)) {
			criteria.andLike("assetsModel", "%"+assetsModel+"%");
		}
		if(useStartDate!=null){
			criteria.andGreaterThanOrEqualTo("useStartTime",useStartDate);
		}
		if(useEndDate!=null){
			criteria.andLessThanOrEqualTo("useStartTime",useEndDate);
		}
		if (!StringUtil.isEmpty(remarks)) {
			criteria.andLike("remarks", "%"+remarks+"%");
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
		List<BusAssets> list = busAssetsService.selectByCondition(condition);
		for(BusAssets asset:list){
			if(asset!=null && asset.getStatus()!=null && asset.getStatus()!=1 && asset.getStatus()!=6){
				BusAssetsAudit audit=busAssetsAuditService.getAssetsAuditById(asset.getId());
				if(audit!=null){
					asset.setAuditUserName(audit.getManagerUserName());
					asset.setAuditUseUserName(audit.getUseUserName());
					asset.setAuditDepositAddress(audit.getDepositAddress());
					asset.setAuditUseDeptName(audit.getReceiveDeptName());
				}
			}
		}
		PageInfo<BusAssets> pageInfo = new PageInfo<>(list);
		return ResultGenerator.success(pageInfo);
    	}catch(Exception e){
    		e.printStackTrace();
    		return ResultGenerator.success();
    	}
	}
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
    	Map<String,Object> map=new HashMap<String,Object>();
        BusAssets busAssets = busAssetsService.selectById(id);
        List<BusTaskAudit> processList=busTaskAuditService.selectByTask(id, 1);
        List<BusAttachments> attList= busAttachmentsService.getAttachmentsList(id, 1);
        Condition condition =  new Condition(BusAssetsLog.class);
        condition.createCriteria().andEqualTo("assetsId", id);
        List<BusAssetsLog> logList=busAssetsLogService.selectByCondition(condition);
        map.put("assets", busAssets);
        map.put("auditList", processList);
        map.put("attList", attList);
        map.put("logList", logList);
        return ResultGenerator.success(map);
	}
    
    @ApiOperation(value = "???????????????????????????????????????", notes = "???????????????????????????????????????")
	@GetMapping("/getAttachments")
    public Result getAttachments() {
    	Condition condition = new Condition(SysDictionary.class);
		Example.Criteria criteria = condition.createCriteria();
			criteria.andEqualTo("dictType", "ASSETSATTR");	
		List<SysDictionary> list = sysDictionaryService.selectByCondition(condition);
        return ResultGenerator.success(list);
	}
    
    @ApiOperation(value = "????????????", notes = "????????????")
	@PostMapping("/add")
    public Result add(@RequestBody BusAssets busAssets) {
    	UserInfo userInfo = ThreadRequestContext.current();
    	Integer count=busAssetsService.getAssetsCount(busAssets.getAssetsNo());
    	if(count>0 && busAssets.getAssetsNo()!=null){
    		   return ResultGenerator.error("????????????????????????,????????????");
    	}
        busAssetsService.saveAssets(userInfo,busAssets);
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
	@GetMapping("/transferList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsNo", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "deptName", value = "??????", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "????????????:asc???desc", dataType = "String")
    })
    public Result transferList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String assetsNo, @RequestParam(required = false) String assetsName
    		, @RequestParam(required = false) String deptName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {
            
    	List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(8);
		return this.search(page, size, assetsNo, assetsName,list,deptName,"yes",orderBy,asc,null);
	}
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@GetMapping("/handoverList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsNo", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "deptName", value = "??????", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "????????????:asc???desc", dataType = "String")
    })
    public Result handoverList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String assetsNo, @RequestParam(required = false) String assetsName
    		, @RequestParam(required = false) String deptName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {           
		List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		return this.search(page, size, assetsNo, assetsName,list,deptName,null,orderBy,asc,null);
	}
    
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@GetMapping("/updateList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsNo", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "deptName", value = "??????", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "????????????:asc???desc", dataType = "String")
    })
    public Result updateList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String assetsNo, @RequestParam(required = false) String assetsName
    		, @RequestParam(required = false) String deptName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {           
		List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		list.add(9);
		list.add(8);
		return this.search(page, size, assetsNo, assetsName,list,deptName,null,orderBy,asc,null);
	}
    
    @ApiOperation(value = "????????????", notes = "????????????")
	@GetMapping("/scrapList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsNo", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "deptName", value = "??????", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "????????????:asc???desc", dataType = "String"),
        @ApiImplicitParam(name = "depositAddress", value = "????????????", dataType = "String")
    })
    public Result scrapList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String assetsNo, @RequestParam(required = false) String assetsName
    		, @RequestParam(required = false) String deptName,@RequestParam(required = false) String  depositAddress,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) { 
		List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		return this.search(page, size, assetsNo, assetsName,list,deptName,null,orderBy,asc,depositAddress);
	}
    
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@GetMapping("/scrapFinishList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "assetsNo", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "assetsName", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "status", value = "??????", dataType = "Integer"),
        @ApiImplicitParam(name = "orderBy", value = "????????????", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "????????????:asc???desc", dataType = "String"),
        @ApiImplicitParam(name = "depositAddress", value = "????????????", dataType = "String")
    })
    public Result scrapFinishList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String assetsNo, @RequestParam(required = false) String assetsName,
    		@RequestParam(required = false) Integer status, @RequestParam(required = false) String deptName,
    		@RequestParam(required = false) String orderBy,@RequestParam(required = false) String  depositAddress,
    		@RequestParam(required = false) String asc) {   
		List<Integer> list=new ArrayList<Integer>();
		if(status!=null){
			list.add(status);
		}else{
			list.add(4);
			list.add(5);
			list.add(6);	
			list.add(7);	
		}
		return this.search(page, size, assetsNo, assetsName,list,deptName,null,orderBy,asc,depositAddress);
	}

    @ApiOperation(value = "????????????", notes = "????????????")
	@PostMapping("/transfer")
    public Result transfer(@RequestBody OptAssetVo optAssetVo) {
    	UserInfo userInfo = ThreadRequestContext.current();
		for(OptAssetUseVo id:optAssetVo.getIds()){	
			BusAssets busAssets=busAssetsService.selectById(id.getId());
//			if(busAssets.getStatus()!=1 ){
//		        return ResultGenerator.error("????????????????????????,????????????");
//			}
		}
		try{
			boolean lock = redisClient.getLock("task:transfer", 2000, 2000, 20 * 1000);
			if(!lock){
				// redis ?????????????????????????????????????????????
		        return ResultGenerator.error("?????????????????????,???????????????");
			}
		 	redisClient.put("task:transfer", "lock");	
		 	SysDept dept=sysDeptService.selectById(userInfo.getDeptIdList().get(0));
	        busAssetsService.updateStatus(optAssetVo.getIds(), 2, "????????????", userInfo.getId().intValue(), userInfo.getNickname(),
	        		userInfo.getDeptIdList().get(0), dept.getName(),1,optAssetVo);
	        redisClient.releaseLock("task:transfer");		
		}catch(Exception e){
			redisClient.releaseLock("task:transfer");
			e.printStackTrace();
		}finally {
			// ?????????????????????key
			redisClient.releaseLock("task:transfer");
		}
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "????????????", notes = "????????????")
	@PostMapping("/handover")
    public Result handover(@RequestBody OptAssetVo optAssetVo) {
    	UserInfo userInfo = ThreadRequestContext.current();
		for(OptAssetUseVo id:optAssetVo.getIds()){	
			BusAssets busAssets=busAssetsService.selectById(id.getId());
//			if(busAssets.getStatus()!=1){
//		        return ResultGenerator.error("????????????????????????,????????????");
//			}
		}
		try{
			boolean lock = redisClient.getLock("task:handover", 2000, 2000, 20 * 1000);
			if(!lock){
				// redis ?????????????????????????????????????????????
		        return ResultGenerator.error("?????????????????????,???????????????");
			}
		 	redisClient.put("task:handover", "lock");	
		 	SysDept dept=sysDeptService.selectById(userInfo.getDeptIdList().get(0));
	        busAssetsService.updateStatus(optAssetVo.getIds(), 3, "????????????", userInfo.getId().intValue(), userInfo.getNickname(),
	        		userInfo.getDeptIdList().get(0), dept.getName(),2,optAssetVo);
	        redisClient.put("task:handover", "lock");
		}catch(Exception e){
			redisClient.put("task:handover", "lock");
			e.printStackTrace();
		}finally {
			// ?????????????????????key
			redisClient.releaseLock("task:handover");
		}
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "????????????", notes = "????????????")
	@PostMapping("/scrap")
    public Result scrap(@RequestBody OptAssetVo optAssetVo) {
    	UserInfo userInfo = ThreadRequestContext.current();
		for(OptAssetUseVo id:optAssetVo.getIds()){	
			BusAssets busAssets=busAssetsService.selectById(id.getId());
//			if(busAssets.getStatus()!=1){
//		        return ResultGenerator.error("????????????????????????,????????????");
//			}
		}
		try{
			boolean lock = redisClient.getLock("task:scrap", 2000, 2000, 20 * 1000);
			if(!lock){
				// redis ?????????????????????????????????????????????
		        return ResultGenerator.error("?????????????????????,???????????????");
			}
		 	redisClient.put("task:scrap", "lock");	
		 	SysDept dept=sysDeptService.selectById(userInfo.getDeptIdList().get(0));
	        busAssetsService.updateStatus(optAssetVo.getIds(), 4, "????????????", userInfo.getId().intValue(), userInfo.getNickname(),
	        		userInfo.getDeptIdList().get(0), dept.getName(),3,optAssetVo);
	        redisClient.releaseLock("task:scrap");
		}catch(Exception e){
			redisClient.releaseLock("task:scrap");
			e.printStackTrace();
		}finally {
			// ?????????????????????key
			redisClient.releaseLock("task:scrap");
		}
        return ResultGenerator.success();
	}

    @ApiOperation(value = "????????????", notes = "????????????")
    @PostMapping("/update")
    public Result update(@RequestBody BusAssets busAssets) {
    	UserInfo userInfo = ThreadRequestContext.current();
		BusAssets assets=busAssetsService.selectById(busAssets.getId());
		busAssets.setCreateTime(assets.getCreateTime());
		if(busAssets.getAssetsNo()!=null && !"".equals(busAssets.getAssetsNo())){
			Integer count=busAssetsService.getAssetsNoCount(busAssets.getAssetsNo(), busAssets.getId());
			if(count>0){
				 return ResultGenerator.error("???????????????????????????????????????");
			}
		}
		if(busAssets.getCardId()!=null && !"".equals(busAssets.getCardId())){
			Integer count=busAssetsService.getCardIdCount(busAssets.getCardId(), busAssets.getId());
			if(count>0){
				 return ResultGenerator.error("???????????????????????????????????????");
			}
		}
//		if(assets.getStatus()!=1){
//		   return ResultGenerator.error("????????????????????????,????????????");
//		}
		try{
			boolean lock = redisClient.getLock("task:update", 2000, 2000, 30 * 1000);
			if(!lock){
				// redis ?????????????????????????????????????????????
		        return ResultGenerator.error("?????????????????????,???????????????");
			}
		 	redisClient.put("task:update", "lock");
		 	SysDept dept=sysDeptService.selectById(userInfo.getDeptIdList().get(0));
	        busAssetsService.updateAssets(userInfo,busAssets,userInfo.getDeptIdList().get(0),dept.getName());
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			// ?????????????????????key
			redisClient.releaseLock("task:update");
		}
        return ResultGenerator.success();
	}
    
    //??????????????????
    private Result search( Integer page, Integer size,
    		 String assetsNo, String assetsName,List<Integer> status,String deptName,String isTransfer,
    		 String orderBy,String asc,String depositAddress){
    	UserInfo userInfo = ThreadRequestContext.current();
		PageHelper.startPage(page, size);
		Condition condition = new Condition(BusAssets.class);
		Example.Criteria criteria = condition.createCriteria();
		if(status!=null && status.size()>0){
			criteria.andIn("status", status);		
		}
		if(!userInfo.getDeptIdList().contains(100)){
			criteria.andIn("useDeptId", userInfo.getDeptIdList());
		}
		if (!StringUtil.isEmpty(assetsNo)) {
			criteria.andLike("assetsNo", "%"+assetsNo+"%");
		}
		if (!StringUtil.isEmpty(assetsName)) {
			criteria.andLike("assetsName", "%"+assetsName+"%");
		}
		if (!StringUtil.isEmpty(deptName)) {
			criteria.andLike("useDeptName", "%"+deptName+"%");
		}
		if (!StringUtil.isEmpty(deptName)) {
			criteria.andLike("useDeptName", "%"+deptName+"%");
		}
		if (!StringUtil.isEmpty(depositAddress)) {
			criteria.andLike("depositAddress", "%"+depositAddress+"%");
		}
		if("yes".equals(isTransfer)){
			criteria.andIsNull("useDeptId");
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
		List<BusAssets> list = busAssetsService.selectByCondition(condition);
		for(BusAssets asset:list){
			if(asset.getStatus()!=1 && asset.getStatus()!=6){
				BusAssetsAudit audit=busAssetsAuditService.getAssetsAuditById(asset.getId());
				if(audit!=null){
					asset.setAuditUserName(audit.getManagerUserName());
					asset.setAuditUseUserName(audit.getUseUserName());
					asset.setAuditDepositAddress(audit.getDepositAddress());
					asset.setAuditUseDeptName(audit.getReceiveDeptName());
				}
			}
		}
		PageInfo<BusAssets> pageInfo = new PageInfo<>(list);
		return ResultGenerator.success(pageInfo);
    }
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@GetMapping("/getRole")
    public Result getRole() {    
    	UserInfo userInfo = ThreadRequestContext.current();
    	if(userInfo.getRoleIdList()!=null){
    		for(Integer role:userInfo.getRoleIdList()){
    			if(role==2){
    		    	return ResultGenerator.success("centermanager");    				
    			}
    		}
    	}
    	return ResultGenerator.success("deptmanager");
	}
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@PostMapping("/updateScrapStatus")
    public Result updateScrapStatus(@RequestBody OptScrapVo vo) {
       	UserInfo userInfo = ThreadRequestContext.current();
       	for(OptAssetUseVo id:vo.getIds()){
       		BusAssets bus=busAssetsService.selectById(id.getId());     
       		if(bus.getStatus()==6){
       		  	busAssetsService.updateStatus(id.getId(), 7, userInfo.getId().intValue(), userInfo.getNickname());	
       		}
     
       	}
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
	@PostMapping("/updateApply")
    public Result updateApply(@RequestBody List<ApplyVo> list) {
       	UserInfo userInfo = ThreadRequestContext.current();
       	for(ApplyVo vo:list){
       		BusAssets bus=busAssetsService.selectById(vo.getId());     
       		if(bus.getStatus()==4){
            	busAssetsService.updateApply(vo.getId(), vo.getNetWorth(),vo.getDecrease(), vo.getExpireYear());	
            	busAssetsService.updateStatus(vo.getId(), 5, userInfo.getId().intValue(), userInfo.getNickname());		
       		}
       	}
        return ResultGenerator.success();
	}
    
    @GetMapping("/delete")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public Result delete(@RequestParam(required = true) Integer id) {
   		BusAssets bus=busAssetsService.selectById(id);   
   		if(bus.getUseDeptId()!=null){
   	        return ResultGenerator.error("????????????????????????,????????????");
   		}
    	busAssetsService.deleteById(id);
        return ResultGenerator.success();
	}
    
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
	@PostMapping("/updateAddress")
    public Result updateAddress(@RequestBody List<AddressVo> list) {
       	UserInfo userInfo = ThreadRequestContext.current();
       	for(AddressVo vo:list){
       		BusAssets bus=busAssetsService.selectById(vo.getId());    
       		busAssetsService.updateAdress(vo.getId(), vo.getAddress());

       	}
        return ResultGenerator.success();
	}
}

