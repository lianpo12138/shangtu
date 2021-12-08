package com.dubu.turnover.domain.enums;

public interface PushEnum {
	/**
	 * 消息类型
	 * 
	 * @author DELL
	 *
	 */
	public enum MessageSubjects implements IEnum {

		RECHARGE("充值"),
		WITHRAWS("提现"),
		PAY("支付"),
		AUCTIOS_STATUS("委托藏品状态变更"),
		NOTPAY("订单不支付"),
		MATCH_SUCCESS("求购成功"),
		END_BIDDING("结标提醒"),
		CONFIRM("合同确认"),
	    BID("出价"),
	    END("截标"),
	    PUSH_PRICE("价格推送"),
	    OPERATIONAL("运营活动");

		private String descr;

		private MessageSubjects(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}
	/**
	 * 推送类型
	 * 
	 * @author DELL
	 *
	 */
	public enum PushTypes implements IEnum {

		JPUSH("极光推送"), 
		WX("微信"), 
		SMS("短信"), 
		NORMAL("普通");

		private String descr;

		private PushTypes(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}
	/**
	 * 消息链接类型
	 * 
	 * @author DELL
	 *
	 */
	public enum MessageLinkTypes implements IEnum {

	    WEB_VIEW("web"), 
	    NATIVE_APP("app"),
	    NOT_LINK("notlink");

		private String descr;

		private MessageLinkTypes(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}
	/**
	 * 推送状态
	 * 
	 * @author DELL
	 *
	 */
	public enum PushStatus implements IEnum {

	    INVALID( "无效"), 
	    VALID("有效"), 
	    READ("已读");
		private String descr;

		private PushStatus(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}
	/**
	 * 推送通道
	 * 
	 * @author DELL
	 *
	 */
	public enum PushChannels implements IEnum {

		IOS("IOS"),
		ANDROID("ANDROID");
		
		
		private String descr;

		private PushChannels(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}
	/**
	 * 消息类型
	 * 
	 * @author DELL
	 *
	 */
	public enum MessageTypes implements IEnum {

		JPUSH_SYSTEM_GROUP("群组消息"), 
		JPUSH_SYSTEM_USER("系统消息"),
		JPUSH_USER("个人消息"),
		JPUSH_NOTICE("通知");
		
		
		private String descr;

		private MessageTypes(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}

    
}
