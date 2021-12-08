package com.dubu.turnover.domain.entity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.dubu.turnover.vo.DeptVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
@Data
public class SysAdmin implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private String loginName;
	private String userName;
	private String email;
	private String phoneNo;
	private String sex;
	private String avatar;
	private String password;
	private String salt;
	private Boolean status;
	private Integer brandId;
	private Boolean delFlag;
	private String loginIp;
	private java.util.Date loginDate;
	private String createBy;
	private java.util.Date createTime;
	private String updateBy;
	private java.util.Date updateTime;
	private String remark;
	private Integer deptId;
	private String deptName;

	@Transient
	private List<SysMenu> menus  =new ArrayList<>();
	@Transient
	private List<SysRole>  roles  =new ArrayList<>();
	@Transient
	private Set<Integer> roleIdList = new HashSet<>();
	@Transient
	private List<DeptVo> deptList = new ArrayList<DeptVo>();

}
