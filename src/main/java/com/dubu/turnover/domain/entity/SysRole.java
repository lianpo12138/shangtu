package com.dubu.turnover.domain.entity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.HashSet;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Data
public class SysRole implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private String roleName;
	private Integer roleSort;
	private boolean status;
	private String createBy;
	private java.util.Date createTime;
	private String updateBy;
	private java.util.Date updateTime;
	private String remark;
	private Integer deptId;
	private String deptName;

	@Transient
	private java.util.Set<Integer> menuIdList = new HashSet<>();
}
