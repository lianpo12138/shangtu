package com.dubu.turnover.service;

import java.util.List;

import com.dubu.turnover.mapper.BusConsumeStoreMapper;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAssetsLog;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeCategory;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusConsumeStoreLog;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusConsumeStoreService extends AbstractService<BusConsumeStore> {

	@Resource
	private BusConsumeStoreMapper busConsumeStoreMapper;
	
    @Resource
	private BusAttachmentsService busAttachmentsService;
    
    @Resource
	private BusAssetsLogService busAssetsLogService;
    
    @Resource
   	private BusConsumeCategoryService busConsumeCategoryService;
    
    @Resource
	private BusConsumeStoreLogService busConsumeStoreLogService;
	
	@Transactional
	public void saveConsumeStore(BusConsumeStore busConsumeStore,UserInfo userInfo){
		BusConsumeCategory category=busConsumeCategoryService.selectById(busConsumeStore.getCategoryId());
		Condition condition = new Condition(BusConsumeStore.class);
		condition.createCriteria().andEqualTo("categoryId", busConsumeStore.getCategoryId())
		         .andEqualTo("storeAddressId", busConsumeStore.getStoreAddressId());
		List<BusConsumeStore> storeList = busConsumeStoreMapper.selectByCondition(condition);
		if(storeList.size()>0){
			busConsumeStoreMapper.updateStoreNumber(storeList.get(0).getId(), 
					busConsumeStore.getNumber());
		}else{
			busConsumeStore.setConsumeModel(category.getConsumeModel());
			busConsumeStore.setConsumeType(category.getConsumeTypeName());
			busConsumeStore.setConsumeName(category.getConsumeName());
			busConsumeStore.setDeptId(category.getDeptId());
			busConsumeStore.setDeptName(category.getDeptName());
			busConsumeStore.setIsAccept(category.getIsAccept()!=null?category.getIsAccept().toString():"0");
			busConsumeStoreMapper.insert(busConsumeStore);
		}
        for(BusAttachments att:busConsumeStore.getAttList()){
        	att.setAttrSourceId(busConsumeStore.getId());
        	att.setAttrSourceType(1);
        	busAttachmentsService.save(att);
        }
        BusAssetsLog log=new BusAssetsLog();
        log.setAssetsId(busConsumeStore.getId());
        log.setAssetsName(busConsumeStore.getConsumeName());
        log.setOptUserId(userInfo.getId().intValue());
        log.setOptUserName(userInfo.getNickname());
        log.setAuctionName("耗材库存新增");
        busAssetsLogService.save(log);
        BusConsumeStoreLog storeLog=new BusConsumeStoreLog();
        storeLog.setCategoryId(category.getId());
        storeLog.setCategoryName(category.getConsumeName());
        storeLog.setNumber(busConsumeStore.getNumber());
        storeLog.setDeptId(category.getDeptId());
        storeLog.setDeptName(category.getDeptName());
        storeLog.setUserId(userInfo.getId().intValue());
        storeLog.setUserName(userInfo.getNickname());
        storeLog.setAddressId(busConsumeStore.getStoreAddressId());
        storeLog.setAddressName(busConsumeStore.getStoreAddress());
        storeLog.setType("in");
        busConsumeStoreLogService.save(storeLog);
	}
	
	
	public void updateApplyNumber(Integer id,Integer number){
		busConsumeStoreMapper.updateApplyNumber(id, number);
	}
	
	
	public void updateStoreNumber(Integer id,Integer number){
		busConsumeStoreMapper.updateStoreNumber(id, number);
	}
	
	public void updateOutStoreNumber(Integer id,Integer number){
		busConsumeStoreMapper.updateOutStoreNumber(id, number);
	}

}
