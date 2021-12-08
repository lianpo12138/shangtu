package com.dubu.turnover.domain;

import java.io.Serializable;
import java.util.Date;

import com.dubu.turnover.domain.entity.UserAccounts;

public class ErpUserVo implements Serializable {

    private static final long serialVersionUID = 4355509153895032252L;

    private UserDetailsInfo userDetailsInfo;

    private UserAccounts userAccounts;
	private Long id;

    private Date createdAt;
    private String createdBy;
    private String updatedBy;
    private Date updatedAt;
    private Long   updatedId;
    private Long   createdId;
    private String	operateName;          // 鎿嶄綔浜哄鍚�
    private String remark;

    public UserDetailsInfo getUserDetailsInfo() {
        return userDetailsInfo;
    }

    public void setUserDetailsInfo(UserDetailsInfo userDetailsInfo) {
        this.userDetailsInfo = userDetailsInfo;
    }

    public UserAccounts getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(UserAccounts userAccounts) {
        this.userAccounts = userAccounts;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getUpdatedId() {
		return updatedId;
	}

	public void setUpdatedId(Long updatedId) {
		this.updatedId = updatedId;
	}

	public Long getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
