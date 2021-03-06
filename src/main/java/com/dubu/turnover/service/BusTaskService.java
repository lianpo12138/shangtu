package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusTaskMapper;
import com.dubu.turnover.utils.EmailUtil;
import com.github.pagehelper.Page;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeLog;
import com.dubu.turnover.domain.entity.BusConsumeStoreLog;
import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.BusMsgTemplate;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.BusTaskProcess;
import com.dubu.turnover.domain.entity.BusTaskRelation;
import com.dubu.turnover.domain.entity.BusTaskSign;
import com.dubu.turnover.domain.entity.SysAdmin;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.controller.erp.SysLoginController;
import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusTaskService extends AbstractService<BusTask> {
	
	private Logger logger = LoggerFactory.getLogger(BusTaskService.class);

	@Resource
	private BusTaskMapper busTaskMapper;

	@Resource
	private BusTaskAuditService busTaskAuditService;

	@Resource
	private BusTaskSignService busTaskSignService;

	@Resource
	private BusAttachmentsService busAttachmentsService;

	@Resource
	private BusAssetsService busAssetsService;

	@Resource
	private BusTaskRelationService busTaskRelationService;

	@Resource
	private BusTaskProcessService busTaskProcessService;

	@Resource
	private BusAssetsAuditService busAssetsAuditService;

	@Resource
	private BusConsumeUseService busConsumeUseService;

	@Resource
	private BusConsumeStoreService busConsumeStoreService;
	
    @Resource
   	private BusMsgTemplateService busMsgTemplateService;
    
	@Resource
	private SysAdminService sysAdminService;
	
    @Resource
	private BusConsumeStoreLogService busConsumeStoreLogService;
    
    @Resource
	private BusAssetsLogService busAssetsLogService;
    
    @Resource
   	private BusConsumeLogService busConsumeLogService;
    
    @Autowired
   	private UserTaskService userTaskService;

	// ?????????
	public Page<BusTask> selectNoTaskTypePage(Integer type, String status, Date startDate, Date endDate,
			Page<BusTask> page, String auditDeptId, String auditRoleIds, Integer userId) {
		busTaskMapper.selectNoTaskTypePage(type, status, startDate, endDate, page, auditDeptId, auditRoleIds, userId);
		return page;
	}

	// ?????????
	public Page<BusTask> selectYesTaskTypePage(Integer type, String status, Date startDate, Date endDate,
			Page<BusTask> page, String auditDeptId, String auditRoleIds, Integer userId) {
		busTaskMapper.selectYesTaskTypePage(type, status, startDate, endDate, page, auditDeptId, auditRoleIds, userId);
		return page;
	}

	public void updateStatus(Integer taskId, Integer status, String remarks) {
		busTaskMapper.updateStatus(taskId, status, remarks);
	}

	// ????????????
	public Page<BusTask> selectSendTypePage(Integer type, String status, Date startDate, Date endDate,
			Page<BusTask> page, String auditDeptId, String auditRoleIds, Integer userId) {
		busTaskMapper.selectSendTypePage(type, status, startDate, endDate, page, auditDeptId, auditRoleIds, userId);
		return page;
	}

	// ?????????
	public Page<BusTask> selectYesSignTypePage(Integer type, String status, Date startDate, Date endDate,
			Page<BusTask> page, String auditDeptId, String auditRoleIds, Integer userId) {
		busTaskMapper.selectYesSignTypePage(type, status, startDate, endDate, page, auditDeptId, auditRoleIds, userId);
		return page;
	}

	// ??????
	@Transactional
	public Result audit(Integer taskId, Integer status, String remarks, String fileUrl, UserInfo userInfo,String fileName) {
		// ??????????????????
		BusTask task = busTaskMapper.selectByPrimaryKey(taskId);
		BusTaskAudit audit = new BusTaskAudit();
		audit.setAuditUserId(userInfo.getId().intValue());
		audit.setAuditUserName(userInfo.getNickname());
		audit.setProcessName(task.getTaskName()+"?????????????????????");
		audit.setType(task.getType());
		audit.setCreateTime(new Date());
		audit.setStatus(status);
		audit.setRemarks(remarks);
		audit.setTaskId(taskId);
		busTaskAuditService.save(audit);
		//??????????????????
		busTaskSignService.updateStatus(taskId, userInfo.getId().intValue(), 1);
		// ??????????????????
		if (status == 2 || task.getNextProcessId() == -1) {
			busTaskMapper.updateStatus(taskId, status, remarks);
		}
		// ??????????????????
		String assetsNos="";
		String assetsNames="";
		String useUserName="";
		String useDeptName="";
		String depositAddress="";
		String managerUserName="";
		if (task.getNextProcessId() == -1  && task.getType() == 1) {
			List<BusTaskRelation> list = busTaskRelationService.getRelationList(taskId);
			for (BusTaskRelation rel : list) {
				BusAssets assets=busAssetsService.selectById(rel.getTaskSourceId());
				assetsNos=assetsNos+assets.getAssetsNo()+",";
				assetsNames=assetsNames+assets.getAssetsName()+",";
				logger.info("???????????????"+assets.getId());
				if(task.getTaskType()==3){
						busAssetsService.updateStatus(rel.getTaskSourceId(), 6, userInfo.getId().intValue(),
								userInfo.getNickname());	
						BusAttachments att = new BusAttachments();
						att.setAttrSourceId(rel.getTaskSourceId());
						att.setAttrSourceType(1);
						att.setAttrType("8");
						att.setAttTypeName("?????????");
						att.setAttrUrl(fileUrl);
						att.setAttrName("???????????????"+fileName);
						busAttachmentsService.save(att);
				}else{
					busAssetsService.updateStatus(rel.getTaskSourceId(), 1, userInfo.getId().intValue(),
							userInfo.getNickname());	
					if(task.getTaskType() == 1){
						busAssetsService.updateTransfer(rel.getTaskSourceId(), 1, userInfo.getId().intValue(),
								userInfo.getNickname());	
					}
					BusAttachments att = new BusAttachments();
					att.setAttrSourceId(rel.getTaskSourceId());
					att.setAttrSourceType(1);
					att.setAttrType("7");
					att.setAttrUrl(fileUrl);
					att.setAttTypeName("?????????");
					att.setAttrName("???????????????"+fileName);
					busAttachmentsService.save(att);
					BusAssetsAudit assetsAudit = busAssetsAuditService.getAssetsAudit(taskId, rel.getTaskSourceId());
					useUserName=assetsAudit.getUseUserName();
					useDeptName=assetsAudit.getReceiveDeptName();
					depositAddress=assetsAudit.getDepositAddress();
					managerUserName=assetsAudit.getManagerUserName();
					BusAssetsAudit audit2=busAssetsAuditService.getAssetsAuditByAssetsId(taskId, rel.getTaskSourceId());
					busAssetsService.updateAssetsAudit(rel.getTaskSourceId(), audit2.getReceiveDeptId(),
							audit2.getReceiveDeptName(), assetsAudit.getManagerUserId(),
							assetsAudit.getManagerUserName(), audit2.getUseUserName(),
							audit2.getDepositAddress(),assetsAudit.getRemarks());
					logger.info("receiveId"+assetsAudit.getReceiveDeptId());
				}
				
				//????????????
		        BusAssetsLog log=new BusAssetsLog();
		        log.setAssetsId(assets.getId());
		        log.setAssetsName(assets.getAssetsName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
		        log.setRemarks(remarks);
		        if(task.getTaskType()==1){
			        log.setAuctionName("???????????????????????????????????????");	
		        }else if(task.getTaskType()==2){
		        	log.setAuctionName("???????????????????????????????????????");	
		        }else if(task.getTaskType()==3){
		        	log.setAuctionName("???????????????????????????????????????");	
		        }else if(task.getTaskType()==4){
		        	log.setAuctionName("???????????????????????????????????????");	
		        }
		        busAssetsLogService.save(log);
			}
		}
		// ????????????
		if (task.getNextProcessId() == -1 && task.getType() == 2) {
			List<BusTaskRelation> list = busTaskRelationService.getRelationList(taskId);
			for (BusTaskRelation rel : list) {
				BusConsumeUse use = busConsumeUseService.selectById(rel.getTaskSourceId());
				busConsumeUseService.updateStatus(rel.getTaskSourceId(), 5, userInfo.getId().intValue(),
						userInfo.getNickname());
				BusAttachments att = new BusAttachments();
				att.setAttrSourceId(rel.getTaskSourceId());
				att.setAttrSourceType(2);
				att.setAttrType("7");
				att.setAttrUrl(fileUrl);
				att.setAttrName("???????????????"+fileName);
				busAttachmentsService.save(att);
				busConsumeStoreService.updateApplyNumber(use.getStoreId(), -use.getNumber());
				busConsumeStoreService.updateStoreNumber(use.getStoreId(), -use.getNumber());
				busConsumeStoreService.updateOutStoreNumber(use.getStoreId(), use.getNumber());
		        BusConsumeStoreLog storeLog=new BusConsumeStoreLog();
		        storeLog.setCategoryId(use.getConsumeCategoryId());
		        storeLog.setCategoryName(use.getConsumeName());
		        storeLog.setNumber(use.getNumber());
		        storeLog.setDeptId(use.getDeptId());
		        storeLog.setDeptName(use.getDeptName());
		        storeLog.setUserId(userInfo.getId().intValue());
		        storeLog.setUserName(userInfo.getNickname());
		        storeLog.setType("out");
		        busConsumeStoreLogService.save(storeLog);
				//????????????
		        BusConsumeLog log=new BusConsumeLog();
		        log.setConsumeId(use.getId());
		        log.setConsumeName(use.getConsumeName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
			    log.setAuctionName("?????????????????????????????????????????????");	
			    log.setRemarks(remarks);
			    busConsumeLogService.save(log);
			}
		}
		// ??????????????????
		if(task.getType() == 1){ 
        //1?????????????????????
    	BusMsgTemplate template=busMsgTemplateService.selectById(task.getTaskType());
		SysAdmin user=sysAdminService.selectById(task.getApplyUserId());
		String msg= template.getAuditContext();			
		if(task.getTaskType()==1){
			 msg=msg.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
						.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName)
						.replace("${value5}", useDeptName);
		}else if (task.getTaskType()==2){
			 msg=msg.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
					    .replace("${value3}",userInfo.getNickname())
						.replace("${value4}",task.getApplyUserName()).replace("${value5}", task.getApplyDeptName())
						.replace("${value6}", useUserName).replace("${value7}", useDeptName);
		}else if (task.getTaskType()==3){
			 msg=msg.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
						.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName);
		}else if (task.getTaskType()==4){
			 msg=msg.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
						.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName)
						.replace("${value5}",  "?????????????????????"+managerUserName+",?????????????????????"+useDeptName
						+"????????????????????????"+depositAddress);
		}
		userTaskService.sendMsg(user.getEmail(), template.getTitle(), msg);
		logger.info("????????????????????????"+user.getEmail());
		//2?????????????????????
		List<BusTaskAudit> auditList=busTaskAuditService.selectByAuditList(task.getId());
		for(BusTaskAudit audit2:auditList){
			if(audit2.getAuditUserId()!=userInfo.getId().intValue()){
				SysAdmin user2=sysAdminService.selectById(audit2.getAuditUserId());
				String msg2= template.getAuditContext();			
				if(task.getTaskType()==1){
					 msg2=msg2.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
								.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName)
								.replace("${value5}", useDeptName);
				}else if (task.getTaskType()==2){
					 msg2=msg2.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
							    .replace("${value3}",userInfo.getNickname())
								.replace("${value4}",task.getApplyUserName()).replace("${value5}", task.getApplyDeptName())
								.replace("${value6}", useUserName).replace("${value7}", useDeptName);
				}else if (task.getTaskType()==3){
					 msg2=msg2.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
								.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName);
				}else if (task.getTaskType()==4){
					 msg2=msg2.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
								.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName)
								.replace("${value5}",  "?????????????????????"+managerUserName+",?????????????????????"+useDeptName
								+"????????????????????????"+depositAddress);
				}
				userTaskService.sendMsg(user2.getEmail(), template.getTitle(), msg2);
				logger.info("??????????????????????????????"+user2.getId()+"_____"+user2.getEmail()+"---"+msg2);	
			}
		}
        //4???????????????
		List<BusTaskSign> list=busTaskSignService.getSignList(task.getId());
		for(BusTaskSign audit2:list){
			SysAdmin user2=sysAdminService.selectById(audit2.getAuditUserId());
			String msg3= template.getAuditContext();			
			if(task.getTaskType()==1){
				 msg3=msg3.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
							.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName)
							.replace("${value5}", useDeptName);
			}else if (task.getTaskType()==2){
				 msg3=msg3.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
						    .replace("${value3}",userInfo.getNickname())
							.replace("${value4}",task.getApplyUserName()).replace("${value5}", task.getApplyDeptName())
							.replace("${value6}", useUserName).replace("${value7}", useDeptName);
			}else if (task.getTaskType()==3){
				 msg3=msg3.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
							.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName);
			}else if (task.getTaskType()==4){
				 msg3=msg3.replace("${value1}", assetsNos).replace("${value2}", assetsNames)
							.replace("${value3}",task.getApplyUserName()).replace("${value4}", useUserName)
							.replace("${value5}",  "?????????????????????"+managerUserName+",?????????????????????"+useDeptName
							+"????????????????????????"+depositAddress);
			}
			if(user2!=null){
				userTaskService.sendMsg(user2.getEmail(), template.getTitle(), msg3);	
				logger.info("????????????????????????"+user2.getId()+"_____"+user2.getEmail()+"-----"+msg3);	
			}
	    }
		}
		return ResultGenerator.success();
	}

	// ??????????????????
	@Transactional
	public Result wxAudit(Integer taskId, Integer status, String remarks, String fileUrl, UserInfo userInfo,
			Integer checkUserId,String fileName) {
		// ??????????????????
		BusTask task = busTaskMapper.selectByPrimaryKey(taskId);
		BusTaskProcess process = busTaskProcessService.selectById(task.getCurrentProcessId());
		BusTaskAudit audit = new BusTaskAudit();
		audit.setAuditUserId(userInfo.getId().intValue());
		audit.setAuditUserName(userInfo.getNickname());
		if(process.getId()==60){
			audit.setProcessName("???????????????????????????????????????");	
		}else if(process.getId()==61){
			audit.setProcessName("????????????????????????????????????????????????");	
		}else if(process.getId()==62){
			audit.setProcessName("???????????????????????????????????????");	
		}else if(process.getId()==63){
			audit.setProcessName("???????????????????????????????????????");	
		}
		audit.setType(task.getType());
		audit.setCreateTime(new Date());
		audit.setStatus(status);
		audit.setRemarks(remarks);
		audit.setTaskId(taskId);
		busTaskAuditService.save(audit);

		// ??????????????????
		if (status == 2 || task.getNextProcessId() == -1) {
			busTaskMapper.updateStatus(taskId, status, remarks);
		}
		String consumeNames="";
		String consumeCategorys="";
		String consumeNums="";
		String consumeUseName="";
		//????????????
		List<BusTaskRelation> list = busTaskRelationService.getRelationList(taskId);
		for (BusTaskRelation rel : list) {
			BusConsumeUse use = busConsumeUseService.selectById(rel.getTaskSourceId());
			consumeNames=consumeNames+use.getConsumeName()+",";
			consumeCategorys=consumeCategorys+use.getConsumeType()+",";
			consumeNums=consumeNums+use.getNumber()+",";
			consumeUseName=use.getUseUserName();
			if(process.getId()==60){
		        BusConsumeLog log=new BusConsumeLog();
		        log.setConsumeId(use.getId());
		        log.setConsumeName(use.getConsumeName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
			    log.setAuctionName("???????????????????????????????????????");	
			    log.setRemarks(remarks);
			    busConsumeLogService.save(log);
			}else if(process.getId()==61){
		        BusConsumeLog log=new BusConsumeLog();
		        log.setConsumeId(use.getId());
		        log.setConsumeName(use.getConsumeName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
			    log.setAuctionName("????????????????????????????????????????????????");	
			    log.setRemarks(remarks);
			    busConsumeLogService.save(log);
			}else if(process.getId()==62){
		        BusConsumeLog log=new BusConsumeLog();
		        log.setConsumeId(use.getId());
		        log.setConsumeName(use.getConsumeName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
			    log.setAuctionName("???????????????????????????????????????");	
			    log.setRemarks(remarks);
			    busConsumeLogService.save(log);
			}else if(process.getId()==63){
		        BusConsumeLog log=new BusConsumeLog();
		        log.setConsumeId(use.getId());
		        log.setConsumeName(use.getConsumeName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
			    log.setAuctionName("???????????????????????????????????????");	
			    log.setRemarks(remarks);
			    busConsumeLogService.save(log);
			}
		}
		//?????????????????????-??????
		if(status==1){
			List<BusTaskRelation> list2 = busTaskRelationService.getRelationList(taskId);
			for (BusTaskRelation rel : list2) {
				BusConsumeUse use = busConsumeUseService.selectById(rel.getTaskSourceId());
				if(process.getId()==60){
					busConsumeUseService.updateStatus(rel.getTaskSourceId(), 2, userInfo.getId().intValue(),
							userInfo.getNickname());	
				}else if(process.getId()==61){
					busConsumeUseService.updateStatus(rel.getTaskSourceId(), 3, userInfo.getId().intValue(),
							userInfo.getNickname());	
				}else if(process.getId()==62){
					busConsumeUseService.updateStatus(rel.getTaskSourceId(), 4, userInfo.getId().intValue(),
							userInfo.getNickname());
					busConsumeUseService.updateCheckUserName(rel.getTaskSourceId(), userInfo.getNickname());
				}else if(process.getId()==63){
					busConsumeUseService.updateStatus(rel.getTaskSourceId(), 5, userInfo.getId().intValue(),
							userInfo.getNickname());
					busConsumeUseService.updateAuditUserName(rel.getTaskSourceId(), userInfo.getNickname());
				}
				BusAttachments att = new BusAttachments();
				att.setAttrSourceId(rel.getTaskSourceId());
				att.setAttrSourceType(2);
				att.setAttrType("8");
				att.setAttrUrl(fileUrl);
				att.setAttrName("???????????????"+fileName);
				busAttachmentsService.save(att);
				if(process.getId()==60){
					busConsumeStoreService.updateApplyNumber(use.getStoreId(), -use.getNumber());
					busConsumeStoreService.updateStoreNumber(use.getStoreId(), -use.getNumber());	
			        BusConsumeStoreLog storeLog=new BusConsumeStoreLog();
			        storeLog.setCategoryId(use.getConsumeCategoryId());
			        storeLog.setCategoryName(use.getConsumeName());
			        storeLog.setNumber(use.getNumber());
			        storeLog.setDeptId(use.getDeptId());
			        storeLog.setDeptName(use.getDeptName());
			        storeLog.setUserId(userInfo.getId().intValue());
			        storeLog.setUserName(userInfo.getNickname());
			        storeLog.setType("out");
			        busConsumeStoreLogService.save(storeLog);
				}
			}
		}
		
		//?????????????????????-?????????
		if(status==2){
			List<BusTaskRelation> list3 = busTaskRelationService.getRelationList(taskId);
			for (BusTaskRelation rel : list3) {
				BusConsumeUse use = busConsumeUseService.selectById(rel.getTaskSourceId());
				busConsumeUseService.updateStatus(rel.getTaskSourceId(), 6, userInfo.getId().intValue(),
						userInfo.getNickname());
				busConsumeStoreService.updateApplyNumber(use.getStoreId(), -use.getNumber());
			}
		}
    	BusMsgTemplate template=busMsgTemplateService.selectById(task.getTaskType());
		// ??????????????????
		if (task.getNextProcessId() != -1) {
			if(process.getId()==60 && status==1){
				    BusTaskProcess process2 = busTaskProcessService.selectById(task.getNextProcessId());
					task.setAuditUserId(task.getApplyUserId());
					task.setNextProcessId(process2.getNextProcessId());
					task.setTaskStepStatus(2);
					task.setCurrentProcessId(process2.getId());
					busTaskMapper.updateByPrimaryKeySelective(task);
					busTaskMapper.updateRole(taskId);
			}
			if(process.getId()==61 && status==1){
				    BusTaskProcess process2 = busTaskProcessService.selectById(task.getNextProcessId());
					task.setAuditUserId(checkUserId);
					task.setNextProcessId(process2.getNextProcessId());
					task.setTaskStepStatus(3);
					task.setCurrentProcessId(process2.getId());
					busTaskMapper.updateByPrimaryKeySelective(task);
					busTaskMapper.updateRole(taskId);
			}
			if(process.getId()==62 && status==1){
				    BusTaskProcess process2 = busTaskProcessService.selectById(task.getNextProcessId());
					task.setAuditRoleId(Integer.valueOf(process2.getCurrentRoleIds()));
					task.setNextProcessId(process2.getNextProcessId());
					task.setTaskStepStatus(4);
					task.setCurrentProcessId(process2.getId());
					busTaskMapper.updateByPrimaryKeySelective(task);
					busTaskMapper.updateUser(taskId);
			}
		}
		//????????????
		if(process.getId()==60 && status==1){
				SysAdmin admin2=sysAdminService.selectById(task.getApplyUserId());
				if(admin2!=null){
					String msg=template.getSendContext();
						msg=msg.replace("${value1}", consumeNames).replace("${value2}", consumeCategorys)
									.replace("${value3}", consumeNums).replace("${value4}", consumeUseName);
							userTaskService.sendMsg(admin2.getEmail(), template.getTitle(), msg);
							logger.info("???????????????????????????"+admin2.getId()+"_____"+admin2.getEmail());
				}
		}else if(process.getId()==61 && status==1){
				SysAdmin admin2=sysAdminService.selectById(checkUserId);
				if(admin2!=null){
					String msg=template.getAuditContext();
					msg=msg.replace("${value1}", consumeNames).replace("${value2}", consumeCategorys)
									.replace("${value3}", consumeNums).replace("${value4}", consumeUseName);
					userTaskService.sendMsg(admin2.getEmail(), template.getTitle(), msg);
					logger.info("???????????????????????????"+admin2.getId()+"_____"+admin2.getEmail());
				}
		}else if(process.getId()==62 && status==1){
			List<SysAdmin> userList=sysAdminService.getAdminByRole(task.getAuditRoleId());
			if(userList.size()>0){
				for(SysAdmin admin2:userList){
								String msg=template.getSignContext();
								msg=msg.replace("${value1}", consumeNames).replace("${value2}", consumeCategorys)
										.replace("${value3}", consumeNums).replace("${value4}", consumeUseName);
								userTaskService.sendMsg(admin2.getEmail(), template.getTitle(), msg);
								logger.info("???????????????????????????"+admin2.getId()+"_____"+admin2.getEmail());
				}
			}
		}else if(process.getId()==63 && status==1){
			List<SysAdmin> userList=sysAdminService.getAdminByRole(task.getAuditRoleId());
			if(userList.size()>0){
				for(SysAdmin admin2:userList){
								String msg=template.getSignContext();
								msg=msg.replace("${value1}", consumeNames).replace("${value2}", consumeCategorys)
										.replace("${value3}", consumeNums).replace("${value4}", consumeUseName);
								userTaskService.sendMsg(admin2.getEmail(), template.getTitle(), msg);
								logger.info("???????????????????????????"+admin2.getId()+"_____"+admin2.getEmail());
				}
			}
		}
		//???????????????
//		SysAdmin user=sysAdminService.selectById(task.getApplyUserId());
//		userTaskService.sendMsg(user.getEmail(), template.getTitle(), template.getSendContext());	
//		logger.info("??????????????????"+user.getId()+"_____"+user.getEmail());
//		//?????????????????????
//		List<BusTaskAudit> auditList=busTaskAuditService.selectByAuditList(task.getId());
//		for(BusTaskAudit audit2:auditList){
//	    	BusMsgTemplate template2=busMsgTemplateService.selectById(task.getTaskType());
//			SysAdmin user2=sysAdminService.selectById(audit2.getAuditUserId());
//			userTaskService.sendMsg(user2.getEmail(), template2.getTitle(), template2.getAuditContext());	
//			logger.info("????????????????????????"+user2.getId()+"_____"+user2.getEmail());
//		}
		
		return ResultGenerator.success();
	}
	
	public Integer  selectNoTaskCount(Integer taskId
			,String auditRoleIds,Integer userId){
		return busTaskMapper.selectNoTaskCount(taskId, auditRoleIds, userId);
	}
	
	//??????
	@Transactional
	public Result sign(Integer taskId, Integer status, String remarks, String fileUrl, UserInfo userInfo,String fileName) {
			// ??????????????????
			BusTask task = busTaskMapper.selectByPrimaryKey(taskId);
			busTaskSignService.updateStatus(taskId, userInfo.getId().intValue(), 1);
			
			List<BusTaskRelation> taskList = busTaskRelationService.getRelationList(task.getId());
			for (BusTaskRelation rel : taskList) {
				//????????????
				BusAssets assets=busAssetsService.selectById(rel.getTaskSourceId());
		        BusAssetsLog log=new BusAssetsLog();
		        log.setAssetsId(assets.getId());
		        log.setAssetsName(assets.getAssetsName());
		        log.setOptUserId(userInfo.getId().intValue());
		        log.setOptUserName(userInfo.getNickname());
		        log.setRemarks(remarks);
		        if(task.getTaskType()==1){
			        log.setAuctionName("???????????????????????????????????????");	
		        }else if(task.getTaskType()==2){
		        	log.setAuctionName("???????????????????????????????????????");	
		        }
		        busAssetsLogService.save(log);
			}

	    	BusMsgTemplate template=busMsgTemplateService.selectById(task.getTaskType());
			//?????????????????????
			SysAdmin user=sysAdminService.selectById(task.getApplyUserId());
			userTaskService.sendMsg(user.getEmail(), template.getTitle(), template.getSendContext());	
			logger.info("??????????????????"+user.getEmail());	
			if(task.getTaskType()==1 || task.getTaskType()==2){
				//?????????????????????
				List<SysAdmin> userList=sysAdminService.getAdminByRole(task.getAuditRoleId());
				if(template!=null && userList.size()>0){
					for(SysAdmin user1:userList){
						//userTaskService.sendMsg(user1.getEmail(), template.getTitle(), template.getContext());	
						//logger.info("??????------????????????????????????"+user1.getId()+"_____"+user1.getEmail());	
					}
				}
				//?????????????????????
				List<BusTaskSign> list=busTaskSignService.getSignList(task.getId());
				for(BusTaskSign audit2:list){
					try{
						if(audit2!=null && audit2.getAuditUserId()!=userInfo.getId().intValue()){
							SysAdmin user2=sysAdminService.selectById(audit2.getAuditUserId());
							//userTaskService.sendMsg(user2.getEmail(), template.getTitle(), template.getAuditContext());	
							logger.info("??????---??????????????????"+user2.getId()+"_____"+user2.getEmail());	
						}	
					}catch(Exception e){
						e.printStackTrace();
						logger.info("??????---??????????????????????????????");
					}
			    }
			}

			return ResultGenerator.success();
		}


}
