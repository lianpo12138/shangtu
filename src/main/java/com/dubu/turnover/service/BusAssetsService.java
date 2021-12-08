package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusAssetsMapper;
import com.dubu.turnover.mapper.BusTaskMapper;
import com.dubu.turnover.utils.DateUtil;
import com.dubu.turnover.utils.EmailUtil;
import com.dubu.turnover.utils.ExcelUtils;
import com.dubu.turnover.utils.ExcelUtils.RowIterator;
import com.dubu.turnover.vo.OptAssetUseVo;
import com.dubu.turnover.vo.OptAssetVo;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAssetsCategory;
import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.BusAssetsType;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusMsgTemplate;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.BusTaskProcess;
import com.dubu.turnover.domain.entity.BusTaskRelation;
import com.dubu.turnover.domain.entity.BusTaskSign;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.SysAdminRole;
import com.dubu.turnover.domain.entity.SysDept;
import com.dubu.turnover.domain.entity.SysRole;

import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.core.ServiceException;
import com.fasterxml.jackson.annotation.JsonFormat;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusAssetsService extends AbstractService<BusAssets> {
	
	private Logger logger = LoggerFactory.getLogger(BusAssetsService.class);
	
	public static List<String> importFields = new ArrayList<String>(){{
		add("financeStatus");
		add("assetsNo");
		add("oriAssetsNo");
		add("assistNo");
		add("assetsName");
		add("assetsCategoryName");
		add("assetsDetailCategoryName");
		add("buyDate");	
		add("financeDate");	
		add("price");	
		add("number");
		add("unit");
		add("worth");
		add("financeIncome");
		add("eduIncome");
		add("otherIncome");
		add("depYear");
		add("totalDepYear");
		add("cost");
		add("priceType");
		add("accountNo");
		add("invoiceNo");
		add("accessType");
		add("useWay");
		add("deptName");
		add("useDeptName");
		add("useUserName");
		add("useStatus");
		add("depositAddress");
		add("assetsModel");
		add("brand");
		add("backup1");
		add("backup2");
		add("backup3");
		add("supplier");
		add("phone");
		add("purchaseType");
		add("useStartTime");
		add("location");
		add("completedDate");
		add("propertyType");
		add("propertyProve");
		add("propertyNo");
		add("propertyNature");
		add("propertyYear");
		add("designWay");
		add("houseStructure");
		add("houseOwn");
		add("selfArea");
		add("borrowArea");
		add("rentArea");
		add("investArea");
		add("otherArea");	
		add("selfCost");
		add("borrowCost");
		add("rentCost");
		add("investCost");
		add("otherCost");
		add("cardDate");
		add("orgInfo");	
		add("carPurpose");
		add("carPlace");
		add("carBrand");
		add("carNo");
		add("carEngineNo");
		add("carExhaust");
		add("carNoType");
		add("carRegisterDate");
		add("contractNo");
		add("addressType");
		add("cardNo");
		add("manufacturer");
		add("press");
		add("pressDate");
		add("archivesNo");
		add("birthYear");
		add("outlineBelong");
		add("sourceAddress");
		add("useYear");
		add("isScrap");
		add("bookDate");
		add("remarks");
		add("busStatus");
		add("obtainDate");
		add("assetsType");
		add("company");
		add("voucherDate");
		add("endDate");
		add("secondCategoryName");
		add("cardAddress");
	}};
	public static List<String> importField = new ArrayList<String>(){{
		add("financeStatus");
		add("assetsNo");
		add("oriAssetsNo");
		add("assistNo");
		add("assetsName");
		add("assetsCategoryName");
		add("assetsDetailCategoryName");
		add("buyDate");
		add("financeDate");
		add("price");
		add("number");
		add("unit");
		add("worth");
		add("financeIncome");
		add("eduIncome");
		add("otherIncome");
		add("depYear");
		add("totalDepYear");
		add("cost");
		add("priceType");
		add("accountNo");
		add("invoiceNo");
		add("accessType");
		add("useWay");
		add("deptName");
		add("useDeptName");
		add("useUserName");
		add("useStatus");
		add("depositAddress");
		add("assetsModel");
		add("brand");
		add("backup1");
		add("backup2");
		add("backup3");
		add("supplier");
		add("phone");
		add("purchaseType");
		add("useStartTime");
		add("location");
		add("completedDate");
		add("propertyType");
		add("propertyProve");
		add("propertyNo");
		add("propertyNature");
		add("propertyYear");
		add("designWay");
		add("houseStructure");
		add("houseOwn");
		add("selfArea");
		add("borrowArea");
		add("rentArea");
		add("investArea");
		add("otherArea");
		add("selfCost");
		add("borrowCost");
		add("rentCost");
		add("investCost");
		add("otherCost");
		add("cardDate");
		add("orgInfo");
		add("carPurpose");
		add("carPlace");
		add("carBrand");
		add("carNo");
		add("carEngineNo");
		add("carExhaust");
		add("carNoType");
		add("carRegisterDate");
		add("contractNo");
		add("addressType");
		add("cardNo");
		add("manufacturer");
		add("press");
		add("pressDate");
		add("archivesNo");
		add("birthYear");
		add("outlineBelong");
		add("sourceAddress");
		add("useYear");
		add("isScrap");
		add("bookDate");
		add("remarks");
		add("busStatus");
		add("obtainDate");
		add("assetsType");
		add("company");
		add("voucherDate");
		add("endDate");
		add("secondCategoryName");
		add("cardAddress");
	}};

	public static List<String> importCompanyFields = new ArrayList<String>(){{
		add("cardId");
		add("assetsNo");
		add("bookAssetsNo");
		add("assetsName");
		add("assetsModel");
		add("useDeptName");
		add("useStartTime");	
		add("price");	
		add("depositAddress");	
		add("useUserName");
		add("userName");
		add("remarks");		
	}};



	@Resource
	private BusAssetsMapper busAssetsMapper;
	
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
	private BusAssetsAuditService busAssetsAuditService;
    
    @Resource
   	private BusTaskSignService  busTaskSignService;
    
    @Resource
   	private BusMsgTemplateService busMsgTemplateService;
    
    @Resource
	private SysDeptService sysDeptService;
    
	@Resource
	private BusTaskAuditService busTaskAuditService;
	
	@Autowired
	private BusAssetsCategoryService busAssetsCategoryService;
	
	@Autowired
   	private  BusAssetsTypeService busAssetsTypeService;
	
	@Autowired
   	private  SysAdminRoleService sysAdminRoleService;
	
	@Autowired
   	private  SysRoleService sysRoleService;
	
	@Autowired
   	private UserTaskService userTaskService;
    
	
	@Transactional
	public void updateStatus(List<OptAssetUseVo> ids,Integer status,String optName,Integer userId,String userName,
			Integer deptId,String deptName,Integer processType,OptAssetVo optAssetVo){
		SysAdmin admin=sysAdminService.selectById(userId);
		BusAssets busAssetName=busAssetsMapper.selectByPrimaryKey(ids.get(0).getId());
		Integer taskId=0;
		Integer roleId=0;
		if(processType!=null && (processType==1 || processType==2 || processType==3)){
			//添加任务
			BusTaskProcess process=busTaskProcessService.selectFirstTask(processType);
			roleId=Integer.valueOf(process.getCurrentRoleIds());
			BusTask task=new BusTask();
			task.setType(1);
			task.setCategoryId(1);
			task.setApplyDeptId(deptId);
			task.setApplyDeptName(deptName);
			task.setApplyUserId(userId);
			task.setApplyUserName(userName);
			task.setAuditRoleId(roleId);
			task.setTaskType(processType);
			task.setTaskName(busAssetName.getAssetsName()+process.getProcessName());
			task.setCurrentProcessId(process.getId());
			task.setNextProcessId(process.getNextProcessId());
			task.setStatus(0);
			task.setTaskStepStatus(1);
			busTaskMapper.insert(task);
			//添加任务关系
			for(OptAssetUseVo id:ids){
				BusTaskRelation relation=new BusTaskRelation();
				relation.setTaskId(task.getId());
				relation.setTaskSourceId(id.getId());
				busTaskRelationService.save(relation);
			}
			//增加签收
			if(processType==1 || processType==2){
				BusTaskSign sign=new BusTaskSign();
				sign.setAuditUserId(optAssetVo.getManagerUserId());
				sign.setStatus(0);
				sign.setTaskId(task.getId());
				sign.setOriTaskId(task.getId());
				busTaskSignService.save(sign);	
		        if(processType==2){
					BusAssets busAssets=busAssetsMapper.selectByPrimaryKey(ids.get(0));
					BusTaskSign sign2=new BusTaskSign();
					sign2.setAuditUserId(busAssets.getUserId());
					sign2.setStatus(0);
					sign2.setTaskId(task.getId());
					sign2.setOriTaskId(task.getId());
					busTaskSignService.save(sign2);	
			    }
			}
			//task
			taskId=task.getId();
		}
		String assetNos="";
		String assetNames="";
		for(OptAssetUseVo id:ids){
			busAssetsMapper.updateStatus(id.getId(),status, userName);	
			BusAssets busAssets=busAssetsMapper.selectByPrimaryKey(id.getId());
			if(busAssets.getAssetsNo()!=null){
				assetNos=assetNos+busAssets.getAssetsNo()+",";	
			}
			if(busAssets.getAssetsName()!=null){
				assetNames=assetNames+busAssets.getAssetsName()+",";	
			}
	        BusAssetsLog log=new BusAssetsLog();
	        log.setAssetsId(busAssets.getId());
	        log.setAssetsName(busAssets.getAssetsName());
	        log.setOptUserId(userId);
	        log.setOptUserName(userName);
	        log.setOptDeptId(deptId);
	        log.setOptDeptName(deptName);
	        log.setAuctionName(optName);
	        busAssetsLogService.save(log);
	        BusAssetsAudit audit=new BusAssetsAudit();
	        audit.setAssetsId(id.getId());
	        audit.setManagerUserId(optAssetVo.getManagerUserId());
	        audit.setManagerUserName(optAssetVo.getManagerUserName());
	        audit.setReceiveDeptId(optAssetVo.getReceiveDeptId());
	        audit.setReceiveDeptName(optAssetVo.getReceiveDeptName());
	        audit.setUseUserId(id.getUseUserId());
	        audit.setUseUserName(id.getUseUserName());
	        audit.setReason(optAssetVo.getReason());
	        audit.setRemarks(optAssetVo.getRemarks());
	        audit.setTaskId(taskId);
	        audit.setDepositAddress(id.getAddress());
	        busAssetsAuditService.save(audit);
		}
		//发送给下一步审核人
		BusMsgTemplate template=busMsgTemplateService.selectById(processType);
		List<SysAdmin> userList=sysAdminService.getAdminByRole(roleId);
		if(template!=null && userList.size()>0){
			for(SysAdmin user:userList){
				try{
					String msg=template.getContext();
					if(processType==1){
						msg=msg.replace("${value1}", assetNos).replace("${value2}", assetNames)
								.replace("${value3}", userName).replace("${value4}", ids.get(0).getUseUserName())
								.replace("${value5}", optAssetVo.getReceiveDeptName());
					}else if(processType==2){
						msg=msg.replace("${value1}", assetNos).replace("${value2}", assetNames)
								.replace("${value3}", userName).replace("${value4}", deptName)
								.replace("${value5}", ids.get(0).getUseUserName())
								.replace("${value6}", optAssetVo.getReceiveDeptName());
					}else if(processType==3){
						msg=msg.replace("${value1}", assetNos).replace("${value2}", assetNames)
								.replace("${value3}", userName).replace("${value4}", deptName);
					}
					userTaskService.sendMsg(user.getEmail(), template.getTitle(), msg);
					logger.info("发起交接下放下一步审核人："+user.getId()+"_____"+user.getEmail()+"-------"+template.getTitle());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		//发送给签收人
		List<BusTaskSign> auditList=busTaskSignService.getSignList(taskId);
		for(BusTaskSign sign2:auditList){
			try{
				SysAdmin user2=sysAdminService.selectById(sign2.getAuditUserId());
				String msg=template.getContext();
				if(processType==1){
					msg=msg.replace("${value1}", assetNos).replace("${value2}", assetNames)
							.replace("${value3}", userName).replace("${value4}", ids.get(0).getUseUserName())
							.replace("${value5}", optAssetVo.getReceiveDeptName());
				}else if(processType==2){
					msg=msg.replace("${value1}", assetNos).replace("${value2}", assetNames)
							.replace("${value3}", userName).replace("${value4}", deptName)
							.replace("${value5}", ids.get(0).getUseUserName())
							.replace("${value6}", optAssetVo.getReceiveDeptName());
				}else if(processType==3){
					msg=msg.replace("${value1}", assetNos).replace("${value2}", assetNames)
							.replace("${value3}", userName).replace("${value4}", deptName);
				}
				logger.info("发起下放交接下一步签收人："+user2.getId()+"_____"+user2.getEmail()+"-------"+template.getTitle());
				userTaskService.sendMsg(user2.getEmail(), template.getTitle(), msg);		
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Transactional
	public  void saveAssets(UserInfo userInfo,BusAssets busAssets){
		busAssets.setStatus(1);
		busAssets.setUserId(userInfo.getId().intValue());
		busAssets.setUserName(userInfo.getNickname());
		if(busAssets.getUseDeptId()==null){
			busAssets.setStatus(8);
		}
		busAssetsMapper.insert(busAssets);
        for(BusAttachments att:busAssets.getAttList()){
        	att.setAttrSourceId(busAssets.getId());
        	att.setAttrSourceType(1);   
        	att.setAttrName("【"+att.getAttTypeName()+"】"+att.getAttrName());
        	busAttachmentsService.save(att);
        }
        BusAssetsLog log=new BusAssetsLog();
        log.setAssetsId(busAssets.getId());
        log.setAssetsName(busAssets.getAssetsName());
        log.setOptUserId(userInfo.getId().intValue());
        log.setOptUserName(userInfo.getNickname());
        log.setAuctionName("资产录入");
        busAssetsLogService.save(log);
	}
	
	@Transactional
	public  void updateAssets(UserInfo userInfo,BusAssets busAssets,Integer deptId,String deptName){
		if(userInfo.getRoleIdList().contains(2)){
			BusAssets oldAssets=busAssetsMapper.selectByPrimaryKey(busAssets.getId());
			busAssetsMapper.updateByPrimaryKeySelective(busAssets);	
			BusMsgTemplate template=busMsgTemplateService.selectById(4);
			if(busAssets.getUseDeptId()!=null){
				List<SysRole> list=sysRoleService.getRolesIdsByDeptId(busAssets.getUseDeptId());
                if(list.size()>0){
                	List<SysAdminRole>  roleList=sysAdminRoleService.getUserListByRold(list.get(0).getId());
                	for(SysAdminRole role:roleList){
                		SysAdmin user=sysAdminService.selectById(role.getUserId());
            			String msg=template.getContext();	
        				msg=msg.replace("${value1}",busAssets.getAssetsNo()!=null?busAssets.getAssetsNo():"")
        						.replace("${value2}", busAssets.getAssetsName())
        						.replace("${value3}", userInfo.getNickname())
        						.replace("${value4}", deptName)
        						.replace("${value5}", busAssets.getDepositAddress()!=null?busAssets.getDepositAddress():"");
        				userTaskService.sendMsg(user.getEmail(), template.getTitle(), msg);
        				logger.info("中心管理员修改下一步审核人："+user.getId()+"_____"+user.getEmail());
                	}
                }
			}
		}else{
			busAssetsMapper.updateStatus(busAssets.getId(), 9, userInfo.getNickname());
			BusTaskProcess process=busTaskProcessService.selectFirstTask(4);
			//添加任务
			int i=0;
			int roleId=0;
//			for(String currentDeptId:process.getCurrentDeptIds().split("\\|")){
//				if(Integer.valueOf(currentDeptId).equals(deptId)){
//					break;
//				}
//				i++;
//			}
//			String[] roleIds=process.getCurrentRoleIds().split("\\|");
//			roleId=Integer.valueOf(roleIds[i]);
			roleId=Integer.valueOf(process.getCurrentRoleIds());
			BusTask task=new BusTask();
			task.setType(1);
			task.setApplyUserId(userInfo.getId().intValue());
			task.setApplyUserName(userInfo.getNickname());
			task.setAuditRoleId(roleId);
			task.setTaskType(4);
			task.setTaskName(busAssets.getAssetsName()+process.getProcessName());
			task.setCurrentProcessId(process.getId());
			task.setNextProcessId(process.getNextProcessId());
			task.setStatus(0);
			task.setApplyDeptName(deptName);
			busTaskMapper.insert(task);
	        BusAssetsAudit audit=new BusAssetsAudit();
	        audit.setAssetsId(busAssets.getId());
	        audit.setManagerUserId(busAssets.getUserId());
	        audit.setManagerUserName(busAssets.getUserName());
	        audit.setUseUserId(busAssets.getUseUserId());
	        audit.setUseUserName(busAssets.getUseUserName());
	        audit.setDepositAddress(busAssets.getDepositAddress());
	        audit.setTaskId(task.getId());
	        busAssetsAuditService.save(audit);
			//添加任务关系
			BusTaskRelation relation=new BusTaskRelation();
			relation.setTaskId(task.getId());
			relation.setTaskSourceId(busAssets.getId());
			busTaskRelationService.save(relation);
			BusMsgTemplate template=busMsgTemplateService.selectById(4);
			List<SysAdmin> userList=sysAdminService.getAdminByRole(roleId);
			if(template!=null && userList.size()>0){
				for(SysAdmin user:userList){
					String msg=template.getContext();			
						msg=msg.replace("${value1}", busAssets.getAssetsNo()).replace("${value2}", busAssets.getAssetsName())
								.replace("${value3}", userInfo.getNickname()).replace("${value4}", deptName)
								.replace("${value5}", "管理人修改为："+busAssets.getUserName()+",使用人修改为："+busAssets.getUseUserName()
								+"存放地点修改为："+busAssets.getDepositAddress());
					userTaskService.sendMsg(user.getEmail(), template.getTitle(), msg);	
					logger.info("下一步审核人："+user.getId()+"_____"+user.getEmail());
				}
			}
		}
        for(BusAttachments att:busAssets.getAttList()){
        	att.setAttrSourceId(busAssets.getId());
        	att.setAttrSourceType(1);
        	if(att.getAttrName().contains("【")==false){
            	att.setAttrName("【"+att.getAttTypeName()+"】"+att.getAttrName());
        	}
        	if(att.getId()!=null &&att.getId()>0){
        		busAttachmentsService.updateById(att);
        	}else{
            	busAttachmentsService.save(att);	
        	}
        }
        BusAssetsLog log=new BusAssetsLog();
        log.setAssetsId(busAssets.getId());
        log.setAssetsName(busAssets.getAssetsName());
        log.setOptUserId(userInfo.getId().intValue());
        log.setOptUserName(userInfo.getNickname());
        log.setAuctionName("资产更正");
        busAssetsLogService.save(log);
	}
	
	@Transactional
	public void updateStatus(Integer id,Integer status,Integer userId,String userName){
		busAssetsMapper.updateStatus(id,status, userName);	
	}
	
	@Transactional
	public void updateAssetsAudit(Integer id,Integer deptId,String deptName,Integer userId,
			String userName,String useUserName,String depositAddress,String remarks){
		busAssetsMapper.updateAssetsAudit(id, deptId, deptName, userId, userName, useUserName,depositAddress,remarks);
	}
	
	@Transactional
	public String importAssets(MultipartFile file,UserInfo userInfo) throws IllegalAccessException, Exception{
    	RowIterator<BusAssets> iterator = ExcelUtils.read(file, BusAssets.class, importFields, 4);
    	List<BusAssets> importAuctionList = new ArrayList<>();
    	while(iterator.hasNext()){
    		BusAssets ia = iterator.next();
    		importAuctionList.add(ia);
    	}

    	//校验
    	int i=4;
    	for(BusAssets j : importAuctionList){
    		i++;
    		logger.info("---------"+i+"---data:"+JSON.toJSONString(j));
    		if(j.getAssetsName()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"资产名称不能为空");			
    		}
    		if(j.getAssetsCategoryName()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"资产大类不能为空");			
    		}
    		if(j.getAssetsDetailCategoryName()==null){
    			throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"资产分类不能为空");	
    		}
    		if(j.getPrice()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"单价不能为空");			
    		}
    		if(j.getNumber()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"数量不能为空");			
    		}
    		if(j.getUseWay()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"用途不能为空");			
    		}
    		if(j.getAssetsModel()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"规格型号不能为空");			
    		}
    		if(j.getDepositAddress()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"存放地点不能为空");			
    		}
    		if(j.getDepositAddress()==null){
          		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"存放地点不能为空");			
    		}
    		if(j.getUseStartTime()==null){
    			throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"使用日期不能为空");
    		}
    		if(j.getUseUserName()==null){
    			throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"使用人不能为空");	
    		}
    		if(j.getFinanceDate()!=null){
    			String str=DateUtil.checkDate(j.getFinanceDate());
    			if("error".equals(str)){
    				throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"财务日期格式错误");		
    			}
    		}
    		if(j.getBuyDate()!=null){
    			String str=DateUtil.checkDate(j.getBuyDate());
    			if("error".equals(str)){
    				throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"购置日期格式错误");		
    			}
    		}
    		if(j.getUseStartTime()!=null){
    			String str=DateUtil.checkDate(j.getUseStartTime());
    			if("error".equals(str)){
    				throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"使用日期格式错误");		
    			}
    		}
    		if(j.getAssetsNo()!=null && j.getOriAssetsNo()!=null && j.getAssetsNo().equals(j.getOriAssetsNo())){
    			throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"资产编号和原资产编号不能相同");	
    		}
    		if(j.getAssetsNo()!=null){
        		Integer count=this.getAssetsCount(j.getAssetsNo());
        		if(count>0){
        			throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+"资产编号已存在,不能录入");	
        		}
    		}
    		if(j.getUseDeptName()!=null){
        		SysDept dept=sysDeptService.selectOne("name", j.getUseDeptName());
        		if(dept==null || dept.getName()==null || "".equals(dept.getName())){
            		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+j.getUseDeptName()+"这个部门在系统不存在");	
        		}
        		j.setUseDeptId(dept.getId());
        		j.setUseDeptName(dept.getName());	
    		}
    		if(j.getAssetsCategoryName()!=null){
        		BusAssetsCategory category=busAssetsCategoryService.selectOne("name", j.getAssetsCategoryName());
        		if(category==null || category.getName()==null || "".equals(category.getName())){
            		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+j.getAssetsCategoryName()+"这个资产大类在系统不存在");	
        		}
        		j.setAssetsCategoryId(category.getId().toString());
        		j.setAssetsCategoryName(category.getName());
    		}
    		
    		if(j.getAssetsDetailCategoryName()!=null){
    			BusAssetsType category=busAssetsTypeService.selectOne("name", j.getAssetsDetailCategoryName());
        		if(category==null || category.getName()==null || "".equals(category.getName())){
            		throw new ServiceException("资产编号:"+j.getAssetsNo()+"所在行:"+j.getAssetsCategoryName()+"这个资产分类在系统不存在");	
        		}
        		j.setAssetsDetailCategoryId(category.getId().toString());
        		j.setAssetsDetailCategoryName(category.getName());
    		}
    	}
    	
    	if(importAuctionList.size() <1){
    		throw new ServiceException("导入excel内容为空！");
    	}
        int m=0;
    	for(BusAssets j : importAuctionList){
    		j.setAssetsBelongId(1);
    		j.setAssetsBelongName("信息处理中心固定资产(馆所)");
    		j.setStatus(1);
    		if(j.getUseDeptId()==null){
    			j.setStatus(8);
    			m++;
    		}
            busAssetsMapper.insert(j);
            BusAssetsLog log=new BusAssetsLog();
            log.setAssetsId(j.getId());
            log.setAssetsName(j.getAssetsName());
            log.setOptUserId(userInfo.getId().intValue());
            log.setOptUserName(userInfo.getNickname());
            log.setAuctionName("资产导入");
            busAssetsLogService.save(log);
    	}
    	int k=importAuctionList.size()-m;
    	return "成功导入"+importAuctionList.size()+"条[馆所]固定资产信息.其中有"+k+"条记录由于包含部门信息,直接下放到对应部门.剩余"+m+"条记录待进行部门分配.";
  
    }
	
	@Transactional
	public String importCompanyAssets(MultipartFile file,UserInfo userInfo) throws IllegalAccessException, Exception{
    	RowIterator<BusAssets> iterator = ExcelUtils.read(file, BusAssets.class, importCompanyFields, 3);
    	List<BusAssets> importAuctionList = new ArrayList<>();
    	while(iterator.hasNext()){
    		BusAssets ia = iterator.next();
    		if(ia.getAssetsName()!=null){
    			importAuctionList.add(ia);	
    		}
    	}

    	//校验
    	int i=3;
    	for(BusAssets j : importAuctionList){
    		i++;
    		if(j.getAssetsName()==null){
          		throw new ServiceException("第"+i+"行:"+"资产名称不能为空");			
    		}
    		if(j.getBookAssetsNo()==null){
          		throw new ServiceException("第"+i+"行:"+"图情资产编号不能为空");			
    		}
    		if(j.getPrice()==null){
          		throw new ServiceException("第"+i+"行:"+"单价不能为空");			
    		}
    		if(j.getAssetsModel()==null){
          		throw new ServiceException("第"+i+"行:"+"规格型号不能为空");			
    		}
    		if(j.getDepositAddress()==null){
          		throw new ServiceException("第"+i+"行:"+"存放地点不能为空");			
    		}
    		if(j.getDepositAddress()==null){
          		throw new ServiceException("第"+i+"行:"+"存放地点不能为空");			
    		}
//    		if(j.getUseStartTime()==null){
//    			throw new ServiceException("第"+i+"行:"+"使用日期不能为空");
//    		}
    		if(j.getUseUserName()==null){
    			throw new ServiceException("第"+i+"行:"+"使用人不能为空");	
    		}
    		if(j.getUserName()==null){
    			throw new ServiceException("第"+i+"行:"+"管理人不能为空");	
    		}
    		if(j.getBuyDate()!=null){
    			String str=DateUtil.checkDate(j.getBuyDate());
    			if("error".equals(str)){
    				throw new ServiceException("第"+i+"行:"+"购置日期格式错误");		
    			}
    		}
    		if(j.getUseStartTime()!=null){
    			String str=DateUtil.checkDate(j.getUseStartTime());
    			if("error".equals(str)){
    				throw new ServiceException("第"+i+"行:"+"使用日期格式错误");		
    			}
    		}
    		if(j.getAssetsNo()!=null && j.getOriAssetsNo()!=null && j.getAssetsNo().equals(j.getOriAssetsNo())){
    			throw new ServiceException("第"+i+"行:"+"资产编号和原资产编号不能相同");	
    		}
    		if(j.getAssetsNo()!=null && j.getBookAssetsNo()!=null && j.getAssetsNo().equals(j.getBookAssetsNo())){
    			throw new ServiceException("第"+i+"行:"+"资产编号和图情资产编号不能相同");	
    		}
    		if(j.getAssetsNo()!=null){
        		Integer count=this.getAssetsCount(j.getAssetsNo());
        		if(count>0){
        			throw new ServiceException("第"+i+"行:"+"资产编号已存在,不能录入");	
        		}
    		}
    		if(j.getUseDeptName()!=null){
        		SysDept dept=sysDeptService.selectOne("name", j.getUseDeptName());
        		if(dept==null || dept.getName()==null || "".equals(dept.getName())){
            		throw new ServiceException("第"+i+"行:"+j.getUseDeptName()+"这个部门在系统不存在");	
        		}
        		j.setUseDeptId(dept.getId());
        		j.setUseDeptName(dept.getName());	
    		}
    	}
    	
    	if(importAuctionList.size() <1){
    		throw new ServiceException("导入excel内容为空！");
    	}
        int m=0;
    	for(BusAssets j : importAuctionList){
    		j.setAssetsBelongId(2);
    		j.setAssetsBelongName("上海图情信息有限公司");
    		j.setStatus(1);
    		if(j.getUseDeptId()==null){
    			j.setStatus(8);
    			m++;
    		}
            busAssetsMapper.insert(j);
            BusAssetsLog log=new BusAssetsLog();
            log.setAssetsId(j.getId());
            log.setAssetsName(j.getAssetsName());
            log.setOptUserId(userInfo.getId().intValue());
            log.setOptUserName(userInfo.getNickname());
            log.setAuctionName("资产导入");
            busAssetsLogService.save(log);
    	}
    	int k=importAuctionList.size()-m;
    	return "成功导入"+importAuctionList.size()+"条[图情公司]固定资产信息.其中有"+k+"条记录由于包含部门信息,直接下放到对应部门.剩余"+m+"条记录待进行部门分配.";
  
    }
	
	public  Integer getAssetsCount(String assetsNo){
    	Condition condition = new Condition(BusAssets.class);
		condition.createCriteria().andEqualTo("assetsNo", assetsNo);
		Integer count=busAssetsMapper.selectCountByCondition(condition);
		return count;
	}
	
	@Transactional
	public void updateTransfer(Integer id,Integer isTransfer,Integer userId,String userName){
		busAssetsMapper.updateTransfer(id,isTransfer, userName);	
	}
	
	@Transactional
	public void updateApply(Integer id,String netWorth,String decrease,String expireYear){
		busAssetsMapper.updateApply(id, netWorth, decrease, expireYear);
	}
	
	@Transactional
	public void updateAdress(Integer id,String address){
		busAssetsMapper.updateAdress(id, address);
	}
	
	public  Integer getAssetsNoCount(String assetsNo,Integer id){
    	Condition condition = new Condition(BusAssets.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("assetsNo", assetsNo);
		criteria.andNotEqualTo("id", id);
		Integer count=busAssetsMapper.selectCountByCondition(condition);
		return count;
	}
	public  Integer getCardIdCount(String cardId,Integer id){
    	Condition condition = new Condition(BusAssets.class);
    	Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("cardId", cardId);
		criteria.andNotEqualTo("id", id);
		Integer count=busAssetsMapper.selectCountByCondition(condition);
		return count;
	}
	
	public  BusAssets getByAssetsNo(String assetsNo){
    	Condition condition = new Condition(BusAssets.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("assetsNo", assetsNo);
		List<BusAssets> list=busAssetsMapper.selectByCondition(condition);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
