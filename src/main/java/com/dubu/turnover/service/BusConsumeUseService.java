package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeUseMapper;
import com.dubu.turnover.mapper.BusTaskMapper;
import com.dubu.turnover.utils.EmailUtil;
import com.dubu.turnover.vo.OptConsumeNumberVo;
import com.dubu.turnover.vo.OptConsumeUseVo;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeAddress;
import com.dubu.turnover.domain.entity.BusConsumeLog;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusConsumeStoreLog;
import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.BusMsgTemplate;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.domain.entity.BusTaskProcess;
import com.dubu.turnover.domain.entity.BusTaskRelation;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.SysDept;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubu.turnover.core.AbstractService;
import com.github.pagehelper.Page;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusConsumeUseService extends AbstractService<BusConsumeUse> {
	
	private Logger logger = LoggerFactory.getLogger(BusConsumeUseService.class);

	@Resource
	private BusConsumeUseMapper busConsumeUseMapper;

	@Resource
	private BusAssetsLogService busAssetsLogService;

	@Resource
	private BusAttachmentsService busAttachmentsService;

	@Resource
	private BusTaskProcessService busTaskProcessService;

	@Resource
	private BusTaskMapper busTaskMapper;

	@Resource
	private SysAdminService sysAdminService;
	
    @Resource
	private BusTaskRelationService busTaskRelationService;
    
    @Resource
	private BusConsumeStoreService busConsumeStoreService;
    
    @Resource
   	private BusMsgTemplateService busMsgTemplateService;
    
    @Resource
   	private BusConsumeLogService busConsumeLogService;
    
    @Resource
	private BusConsumeStoreLogService busConsumeStoreLogService;
    
    @Resource
	private SysDeptService sysDeptService;
    
	@Autowired
   	private UserTaskService userTaskService;
	
    
    @Resource
	private BusAssetsService busAssetsService;
    
    @Resource
	private BusConsumeAddressService busConsumeAddressService;
    
    

	@Transactional
	public void saveConsume(OptConsumeUseVo optConsumeUseVo,UserInfo userInfo,Integer isWenxian,Integer deptId) {
		List<Integer> ids=new ArrayList<Integer>();
		String consumeNames="";
		String consumeCategorys="";
		String consumeNums="";
		String consumeName="";
		int zz=0;
		for(OptConsumeNumberVo vo:optConsumeUseVo.getNumList()){
			BusConsumeStore store=busConsumeStoreService.selectById(vo.getId());
			consumeNames=consumeNames+store.getConsumeName()+",";
			consumeCategorys=consumeCategorys+store.getConsumeType()+",";
			consumeNums=consumeNums+store.getNumber()+",";
			if(zz==0){
				consumeName=store.getConsumeName();
			}
			zz++;
			busConsumeStoreService.updateApplyNumber(vo.getId(), vo.getNumber());
			BusConsumeUse use=new BusConsumeUse();
			use.setStoreId(vo.getId());
			//use.setUseUserId(userInfo.getId().intValue());
			use.setUseUserName(optConsumeUseVo.getUserName());
			use.setNumber(vo.getNumber());
			use.setApplyTime(new Date());
			if(isWenxian!=1){
				use.setStatus(1);	 
			}else{
				use.setStatus(1);
			}
			use.setDeptId(store.getDeptId());
			use.setDeptName(store.getDeptName());
			use.setProjectId(store.getProjectId());
			use.setProjectName(store.getProjectName());
			use.setConsumeCategoryId(store.getCategoryId());
			use.setConsumeName(store.getConsumeName());
			use.setConsumeModel(store.getConsumeModel());
			use.setConsumeType(store.getConsumeType());
			use.setPurpose(optConsumeUseVo.getPurpose());
			use.setConsumeModel(store.getConsumeModel());
			use.setStoreAddressId(store.getStoreAddressId());
			use.setStoreAddress(store.getStoreAddress());
			use.setRemarks(optConsumeUseVo.getRemarks());
			if(optConsumeUseVo.getAssetsNo()!=null && !"".equals(optConsumeUseVo.getAssetsNo())){
				BusAssets assets=busAssetsService.getByAssetsNo(optConsumeUseVo.getAssetsNo());
				if(assets!=null){
					use.setAssetsNo(assets.getAssetsNo());
					use.setAssetsName(assets.getAssetsName());
					use.setAssetsModel(assets.getAssetsModel());	
				}
			}
			busConsumeUseMapper.insert(use);
			//增加日志
	        BusConsumeLog log=new BusConsumeLog();
	        log.setConsumeId(use.getId());
	        log.setConsumeName(use.getConsumeName());
	        log.setOptUserId(userInfo.getId().intValue());
	        log.setOptUserName(userInfo.getNickname());
		    log.setAuctionName("耗材领用申请");	
		    busConsumeLogService.save(log);
			ids.add(use.getId());
		}
        if(isWenxian==1){
    		SysDept dept=sysDeptService.selectById(deptId);
   		    BusTaskProcess process = busTaskProcessService.selectFirstTask(6);
			//添加任务
			int i=0;
			int roleId=0;
			for(String currentDeptId:process.getCurrentDeptIds().split("\\|")){
				if(Integer.valueOf(currentDeptId).equals(deptId)){
					break;
				}
				i++;
			}
			String[] roleIds=process.getCurrentRoleIds().split("\\|");
			roleId=Integer.valueOf(roleIds[i]);
    		BusTask task = new BusTask();
    		task.setApplyDeptId(deptId);
    		task.setApplyDeptName(dept.getName());
    		task.setApplyUserId(userInfo.getId().intValue());
    		task.setApplyUserName(userInfo.getNickname());
    		task.setAuditRoleId(roleId);
    		task.setTaskType(6);
    		task.setTaskName(consumeName+process.getProcessName());
    		task.setCurrentProcessId(process.getId());
    		task.setNextProcessId(process.getNextProcessId());
    		task.setStatus(0);
    		task.setType(2);
    		task.setTaskStepStatus(1);
    		task.setCategoryId(1);
    		busTaskMapper.insert(task);
    		//添加任务关系
    		for(Integer id:ids){
    		    BusTaskRelation relation=new BusTaskRelation();
    			relation.setTaskId(task.getId());
    			relation.setTaskSourceId(id);
    			busTaskRelationService.save(relation);	
    		}
    		BusMsgTemplate template=busMsgTemplateService.selectById(6);
			List<SysAdmin> userList=sysAdminService.getAdminByRole(roleId);
			if(template!=null && userList.size()>0){
				for(SysAdmin user:userList){
					String msg=template.getContext();
						msg=msg.replace("${value1}", consumeNames).replace("${value2}", consumeCategorys)
								.replace("${value3}", consumeNums).replace("${value4}", optConsumeUseVo.getUserName());
						userTaskService.sendMsg(user.getEmail(), template.getTitle(),msg);	
					logger.info("通知下一步审核人："+user.getId()+"_____"+user.getEmail());	
				}
			}
        }else{
    		SysDept dept=sysDeptService.selectById(deptId);
    		BusTaskProcess process = busTaskProcessService.selectFirstTask(5);
    		int i=0;
			int roleId=0;
			for(String currentDeptId:process.getCurrentDeptIds().split("\\|")){
				if(Integer.valueOf(currentDeptId).equals(deptId)){
					break;
				}
				i++;
			}
			String[] roleIds=process.getCurrentRoleIds().split("\\|");
			roleId=Integer.valueOf(roleIds[i]);
    		BusTask task = new BusTask();
    		task.setApplyDeptId(deptId);
    		task.setApplyDeptName(dept.getName());
    		task.setApplyUserId(userInfo.getId().intValue());
    		task.setApplyUserName(userInfo.getNickname());
    		task.setAuditRoleId(roleId);
    		task.setTaskType(5);
    		task.setTaskName(consumeName+process.getProcessName());
    		task.setCurrentProcessId(process.getId());
    		task.setNextProcessId(process.getNextProcessId());
    		task.setStatus(0);
    		task.setType(2);
    		task.setCategoryId(1);
    		busTaskMapper.insert(task);
    		//添加任务关系
    		for(Integer id:ids){
    		    BusTaskRelation relation=new BusTaskRelation();
    			relation.setTaskId(task.getId());
    			relation.setTaskSourceId(id);
    			busTaskRelationService.save(relation);	
    		}	
//    		BusMsgTemplate template=busMsgTemplateService.selectById(5);
//			List<SysAdmin> userList=sysAdminService.getAdminByRole(roleId);
//			if(template!=null && userList.size()>0){
//				for(SysAdmin user:userList){
//					EmailUtil.send_email(user.getEmail(), template.getTitle(), template.getContext());	
//					logger.info("通知下一步审核人："+user.getId()+"_____"+user.getEmail());	
//				}
//			}
        }
		

	}
	
	public Page<BusConsumeUse> selectConsumeUsePage(@Param("consumeCode")String consumeCode,@Param("consumeName")String consumeName,
			@Param("userId")Integer userId,@Param("userName")String userName,@Param("page")Page<BusConsumeUse> page){
		busConsumeUseMapper.selectConsumeUsePage(consumeCode, consumeName, userId, userName, page);
		return page;
	}
	
	public void updateStatus(Integer id,Integer status,Integer userId,String userName){
		busConsumeUseMapper.updateStatus(id, status, userId, userName);
	}
	
	public void updateCheckUserName(Integer id,String checkUserName){
		busConsumeUseMapper.updateCheckUserName(id, checkUserName);
	}
	
	public void updateAuditUserName(Integer id,String auditUserName){
		busConsumeUseMapper.updateAuditUserName(id, auditUserName);
	}

}
