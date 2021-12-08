package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAssetsAuditService;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.service.BusAttachmentsService;
import com.dubu.turnover.service.BusConsumeUseService;
import com.dubu.turnover.service.BusTaskAuditService;
import com.dubu.turnover.service.BusTaskProcessService;
import com.dubu.turnover.service.BusTaskRelationService;
import com.dubu.turnover.service.BusTaskService;
import com.dubu.turnover.service.BusTaskSignService;
import com.dubu.turnover.service.SysAdminService;
import com.dubu.turnover.service.UserAuthService;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.BusTaskProcess;
import com.dubu.turnover.domain.entity.BusTaskRelation;
import com.dubu.turnover.domain.entity.BusTaskSign;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.SysDept;
import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.Page;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/erp/bustask")
@Api(value = "我的任务",tags="我的任务")
public class BusTaskController {
	
    private static Logger log = LoggerFactory.getLogger(BusTaskController.class);
	
	@Autowired
	private SysAdminService adminsService;

    @Resource
	private BusTaskService busTaskService;
    
    @Resource
	private BusAssetsService busAssetsService;
    
    @Resource
	private BusTaskProcessService busTaskProcessService;
    
    @Resource
  	private BusConsumeUseService busConsumeUseService;
    
    @Resource
	private BusTaskRelationService busTaskRelationService;
    
    @Resource
	private BusTaskAuditService busTaskAuditService;
    
    @Resource
   	private BusTaskSignService busTaskSignService;
    
    @Resource
	private BusAttachmentsService busAttachmentsService;
    
	@Resource
	private BusAssetsAuditService busAssetsAuditService;
    
    
    
    
	@InitBinder
    public void initDateFormate(WebDataBinder dataBinder) {
        dataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }
	
	
	@GetMapping("/myAudit")
    @ApiOperation(value = "待审核", notes = "待审核")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "type", value = "任务分类", dataType = "Integer"),
        @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
        @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "Date")
    })
    public Result myAudit(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, 
    		@RequestParam(required = false) Integer type,
    		@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate) {
		UserInfo userInfo = ThreadRequestContext.current();
		String roleIds=userInfo.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		String deptIds=userInfo.getDeptIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		Page<BusTask> searchPage=PageHelper.startPage(page, size);
        List<BusTask> list = busTaskService.selectNoTaskTypePage(type, "1", startDate, endDate, searchPage, deptIds,
        		roleIds,userInfo.getId().intValue());
        PageInfo<BusTask> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}


	@GetMapping("/mySend")
    @ApiOperation(value = "我发起的", notes = "我发起的")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "type", value = "任务分类", dataType = "Integer"),
        @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
        @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "Date")
    })
    public Result mySend(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, 
    		@RequestParam(required = false) Integer type,
    		@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate) {
		UserInfo userInfo = ThreadRequestContext.current();
		String roleIds=userInfo.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		String deptIds=userInfo.getDeptIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		Page<BusTask> searchPage=PageHelper.startPage(page, size);
        List<BusTask> list = busTaskService.selectSendTypePage(type, "1", startDate, endDate, searchPage, deptIds,
        		roleIds,userInfo.getId().intValue());
        PageInfo<BusTask> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

	
	@GetMapping("/finishSign")
    @ApiOperation(value = "已签收", notes = "已签收")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "type", value = "任务分类", dataType = "Integer"),
        @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
        @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "Date")
    })
    public Result finishSign(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, 
    		@RequestParam(required = false) Integer type,
    		@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate) {
		UserInfo userInfo = ThreadRequestContext.current();
		String roleIds=userInfo.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		String deptIds=userInfo.getDeptIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		Page<BusTask> searchPage=PageHelper.startPage(page, size);
        List<BusTask> list = busTaskService.selectYesSignTypePage(type, "2", startDate, endDate, searchPage, deptIds,
        		roleIds,userInfo.getId().intValue());
        PageInfo<BusTask> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}
		
	
	@GetMapping("/finishAudit")
    @ApiOperation(value = "已审核", notes = "已审核")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "type", value = "任务分类", dataType = "Integer"),
        @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
        @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "Date")
    })
    public Result finishAudit(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, 
    		@RequestParam(required = false) Integer type,
    		@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate) {
		UserInfo userInfo = ThreadRequestContext.current();
		String roleIds=userInfo.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		String deptIds=userInfo.getDeptIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
		Page<BusTask> searchPage=PageHelper.startPage(page, size);
        List<BusTask> list = busTaskService.selectYesTaskTypePage(type, "2", startDate, endDate, searchPage, deptIds,
        		roleIds,userInfo.getId().intValue());
        PageInfo<BusTask> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}
	
	
    @ApiOperation(value = "审核-审核详情", notes = "审核-审核详情")
    @GetMapping("/assetsDetail")
    public Result assetsDetail(@RequestParam(required = true)Integer taskId) {
    	Map<String,Object> map=new HashMap<String,Object>();
    	try{
    		UserInfo userInfo = ThreadRequestContext.current();
    		String roleIds=userInfo.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
        	BusTask task=busTaskService.selectById(taskId);
            map.put("task", task);
            Condition condition =  new Condition(BusTaskRelation.class);
    		Example.Criteria criteria = condition.createCriteria();
    			criteria.andEqualTo("taskId", taskId);	
    		List<BusTaskRelation> relationList=busTaskRelationService.selectByCondition(condition);
    		String ids= relationList.stream().map(BusTaskRelation::getTaskSourceId).map(String::valueOf).collect(Collectors.joining(","));
        	if(task.getType()==1){
        		List<BusAssets> assetsList = busAssetsService.selectByIds(ids);
        		List<BusAssetsAudit> assetsAudit = busAssetsAuditService.getAssetsAuditByList(taskId);
        		for(BusAssets asset:assetsList){
        			for(BusAssetsAudit audit:assetsAudit){
        				if(asset.getId().equals(audit.getAssetsId())){
        					asset.setAuditDepositAddress(audit.getDepositAddress());
        					asset.setAuditUseUserName(audit.getUseUserName());
        					asset.setAuditUseDeptName(audit.getReceiveDeptName());
        					asset.setAuditUserName(audit.getManagerUserName());
        				}
        			}
        		}
                map.put("assetsList", assetsList);	
        		BusAssetsAudit assetsAudit2 = busAssetsAuditService.getAssetsAuditByTask(taskId);
                map.put("assetsAudit", assetsAudit2);	
        	}else{
        		List<BusConsumeUse> consumeList = busConsumeUseService.selectByIds(ids);
                map.put("consumeList", consumeList);	
        	}
            List<BusTaskAudit> auditList=busTaskAuditService.selectByAuditList(taskId);
            map.put("auditList", auditList);
    		Integer count=busTaskService.selectNoTaskCount(taskId, roleIds, userInfo.getId().intValue());
            if(count>0 && task.getTaskType()!=4 && task.getTaskType()!=6){
            	map.put("isFile", "yes");
            }else if(task.getTaskType()==6 && task.getTaskStepStatus()==4){
            	map.put("isFile", "yes");
            }else{
            	map.put("isFile", "no");
            }
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error("error", e);
    	}
        return ResultGenerator.success(map);
	}
    
    @ApiOperation(value = "审核", notes = "审核")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Integer"),
        @ApiImplicitParam(name = "status", value = "1通过2不通过", dataType = "Integer"),
        @ApiImplicitParam(name = "remarks", value = "备注", dataType = "String"),
        @ApiImplicitParam(name = "fileUrl", value = "fileUrl", dataType = "String"),
        @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String")
    })
    @GetMapping("/audit")
    public Result audit(@RequestParam(required = true) Integer taskId,
    		@RequestParam(defaultValue = "1") Integer status,@RequestParam(required = false) String remarks
    		,@RequestParam(required = false) String fileUrl,@RequestParam(required = false) String fileName) {
    	try{
    		UserInfo userInfo = ThreadRequestContext.current();
    		String roleIds=userInfo.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
    		Integer count=busTaskService.selectNoTaskCount(taskId, roleIds, userInfo.getId().intValue());
            if(count>0){
                return busTaskService.audit(taskId, status, remarks, fileUrl, userInfo,fileName);
            }else{
                return busTaskService.sign(taskId, status, remarks, fileUrl, userInfo,fileName);	
            }	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ResultGenerator.error("操作错误");
	}
    
  
    
    @ApiOperation(value = "文献班组审核", notes = "文献班组审核")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Integer"),
        @ApiImplicitParam(name = "status", value = "1通过2不通过", dataType = "Integer"),
        @ApiImplicitParam(name = "remarks", value = "备注", dataType = "String"),
        @ApiImplicitParam(name = "userId", value = "验收人", dataType = "Integer"),
        @ApiImplicitParam(name = "fileUrl", value = "fileUrl", dataType = "String"),
        @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String")
    })
    @GetMapping("/wxAudit")
    public Result wxAudit(@RequestParam(required = true) Integer taskId,
    		@RequestParam(defaultValue = "1") Integer status,@RequestParam(required = false) String remarks,
    		@RequestParam(required = false) Integer userId,
    		@RequestParam(required = false) String fileUrl,
    		@RequestParam(required = false) String fileName) {
		UserInfo userInfo = ThreadRequestContext.current();	
		busTaskService.wxAudit(taskId, status, remarks, fileUrl, userInfo, userId,fileName);
	    return ResultGenerator.success();
	}
    
    @ApiOperation(value = "文献班组审核获取验收人", notes = "文献班组审核获取验收人")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Integer")
    })
    @GetMapping("/getCheckUser")
    @NoLogin
    public Result getCheckUser(@RequestParam(required = true) Integer taskId) {
    	try{
    		BusTask task=busTaskService.selectById(taskId);
            List<BusTaskAudit> auditList=busTaskAuditService.selectByAuditList(taskId);
    		 List<SysAdmin> list=adminsService.getAdminByDept(8,null);
    		 List<SysAdmin> roleList=adminsService.getAdminByRole(38);
    		 Iterator<SysAdmin> iterator = list.iterator();
    	        while (iterator.hasNext()) {
    	        	SysAdmin admin = iterator.next();
    				 if(admin.getId()==task.getApplyUserId()){
    					 iterator.remove();
    				 }
    				 if(auditList.size()>0){
    					 for(BusTaskAudit audit:auditList){
    						 if(audit.getAuditUserId()==admin.getId()){
    							 iterator.remove(); 
    						 }
    					 }
    				 }
    				 if(roleList.size()>0){
    					 for(SysAdmin role:roleList){
    						 if(role.getId()==admin.getId()){
    							 iterator.remove(); 
    						 }
    					 }
    				 }
    	        }
    			return ResultGenerator.success(list);
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error("error", e);
    	}
		
		return ResultGenerator.success();	
	}
    
}

