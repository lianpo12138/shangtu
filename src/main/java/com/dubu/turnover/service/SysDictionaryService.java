package com.dubu.turnover.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.domain.entity.SysDictionary;
import com.dubu.turnover.mapper.SysDictionaryMapper;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Service
public class SysDictionaryService extends AbstractService<SysDictionary> {

	@Resource
	private SysDictionaryMapper sysDictionaryMapper;

}
