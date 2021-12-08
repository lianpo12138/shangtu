package com.dubu.turnover.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

public class SysDictionaryVo {

	private String dictCode;

	private String dictName;
	
	private List<SysDictionaryVo> columnList = new ArrayList<SysDictionaryVo>();

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public List<SysDictionaryVo> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<SysDictionaryVo> columnList) {
		this.columnList = columnList;
	}

	
	
}
