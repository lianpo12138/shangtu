package com.dubu.turnover.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.domain.entity.SysSettings;
import com.dubu.turnover.mapper.SysSettingsMapper;

/*
 *  @author: smart boy
 *  @date: 2019-04-02
 */
@Service
public class SysSettingsService extends AbstractService<SysSettings> {

	@Resource
	private SysSettingsMapper sysSettingsMapper;

}
