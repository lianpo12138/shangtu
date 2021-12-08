package com.dubu.turnover.component.pay;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "config.pay.unionpay")
public class PayConfig {
	private String mid;
	private String tid;
	private String notifyurl;
	private String returnurl;  
	private String bankurl;
	private String publickey;
	private String msgsrc;
	private String msgsrcid;
	private String mid4;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}


	public String getBankurl() {
		return bankurl;
	}

	public void setBankurl(String bankurl) {
		this.bankurl = bankurl;
	}

	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	public String getMsgsrc() {
		return msgsrc;
	}

	public void setMsgsrc(String msgsrc) {
		this.msgsrc = msgsrc;
	}

	public String getMsgsrcid() {
		return msgsrcid;
	}

	public void setMsgsrcid(String msgsrcid) {
		this.msgsrcid = msgsrcid;
	}

	public String getMid4() {
		return mid4;
	}

	public void setMid4(String mid4) {
		this.mid4 = mid4;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}

	
}
