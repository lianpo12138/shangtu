package com.dubu.turnover.domain.entity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.dubu.turnover.domain.TreeVo;
/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Data
public class SysMenu extends TreeVo implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer parentId;
	private String name;
	private String type;
	private Boolean visible;
	private String perms;
	private Integer sort;
	private String icon;
	private String createBy;
	private java.util.Date createTime;
	private String updateBy;
	private java.util.Date updateTime;
	private String remark;

}
