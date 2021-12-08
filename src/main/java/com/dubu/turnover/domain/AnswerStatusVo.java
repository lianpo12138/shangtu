package com.dubu.turnover.domain;

import java.io.Serializable;

public class AnswerStatusVo implements Serializable {
	private String answerStatus;
	private String toUrl;
	private String userId;
	private String screenId;
	public AnswerStatusVo(String answerStatus,String toUrl){
		this.answerStatus=answerStatus;
		this.toUrl=toUrl;
	}
	public AnswerStatusVo(String answerStatus,String toUrl,String userId,String screenId){
		this.answerStatus=answerStatus;
		this.toUrl=toUrl;
		this.userId=userId;
		this.screenId=screenId;
	}

	public String getAnswerStatus() {
		return answerStatus;
	}

	public void setAnswerStatus(String answerStatus) {
		this.answerStatus = answerStatus;
	}

	public String getToUrl() {
		return toUrl;
	}

	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getScreenId() {
		return screenId;
	}
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}



}
