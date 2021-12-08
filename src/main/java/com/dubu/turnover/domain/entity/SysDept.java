package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;

import com.dubu.turnover.domain.TreeVo;
/*
 *  @author: smart boy
 *  @date: 2021-03-18
 */
@Data
public class SysDept extends TreeVo implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer parentId;
	private java.lang.String name;
	private java.lang.String type;
	private java.lang.Integer visible;
	private java.lang.String showDeptIds;
	private java.lang.Integer sort;
	private java.lang.String icon;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	private java.lang.String remark;

}
