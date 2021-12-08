package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusAttachmentsMapper;

import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusTaskAudit;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusAttachmentsService extends AbstractService<BusAttachments> {

	@Resource
	private BusAttachmentsMapper busAttachmentsMapper;
	
	public List<BusAttachments> getAttachmentsList(Integer attId,Integer type){
		Example con = new Example(BusAttachments.class);
		con.createCriteria().andEqualTo("attrSourceId",attId).andEqualTo("attrSourceType",type);
	    List<BusAttachments> list = busAttachmentsMapper.selectByCondition(con);
	    return  list;
	}

}
