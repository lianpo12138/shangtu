package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusMsgTemplateMapper;
import com.dubu.turnover.domain.entity.BusMsgTemplate;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-13
 */
@Service
public class BusMsgTemplateService extends AbstractService<BusMsgTemplate> {

	@Resource
	private BusMsgTemplateMapper busMsgTemplateMapper;

}
