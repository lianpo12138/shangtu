package com.dubu.turnover.domain.entity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dubu.turnover.domain.SysDictionaryVo;
/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Data
@Table(name = "sys_dict")
public class SysDictionary implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private String dictId;

    private String dictPid;

    private String dictType;

    private String dictCode;

    private String dictName;

    private String dictFlag;

    private Integer dictSeq;

    private String dictPcode;

    private String description;

}
