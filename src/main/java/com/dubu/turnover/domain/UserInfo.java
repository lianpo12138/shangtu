package com.dubu.turnover.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserInfo {

	private Long Id;
	private String nickname;
	private List<Integer> deptIdList = new ArrayList<>();
	private List<Integer> roleIdList = new ArrayList<>();

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<Integer> getDeptIdList() {
		return deptIdList;
	}

	public void setDeptIdList(List<Integer> deptIdList) {
		this.deptIdList = deptIdList;
	}

	public List<Integer> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
	}

}
