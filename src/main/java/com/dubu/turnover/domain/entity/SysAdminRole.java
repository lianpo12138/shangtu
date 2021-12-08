package com.dubu.turnover.domain.entity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Data
public class SysAdminRole implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer userId;
	private Integer roleId;

}
