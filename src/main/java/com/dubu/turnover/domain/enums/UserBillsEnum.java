package com.dubu.turnover.domain.enums;
/**
 *
 */
public interface UserBillsEnum  {
	 /**
     * 支付方式
     */
    public enum PayMethod implements IEnum {

        ALIPAY("支付宝"),
        WEIXINPAY("微信支付"),
        UNIONPAY("银联支付"),
        BALANCE("余额支付");

        private String descr;

        private PayMethod(String descr) {
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
     * 财务类型科目
     */
    public enum FlowType implements IEnum {

    	RECHARGE("充值"),
        PAY("订单支付"),
        LOGISTICS("物流费支付"),
        TAKE("提货费"),
        PENALTY("违约金"),
        WITHDRAWS("余额提现"),
        SETTLEMENT("结算"),
        REVERT("退款");

        private String descr;

        private FlowType(String descr) {
            this.descr = descr;
        }

        public String getName() {
            return super.name();
        }

        public String getDescr() {
            return descr;
        }
    }
    
	public enum BillStatus{
		DELETED, SHOW, HIDDEN;

		public int getValue() {
			return this.ordinal();
		}

		public String getName() {
			return this.name();
		}
	}
	/**
	 * 账单大科目
	 * 
	 * @author DELL
	 *
	 */
	public enum BillCode implements IEnum {

		//充值
		RECHARGE("充值"),
		//提现
		WITHDRAW("提现"),
		//在线余额支付
		PAY("支付单支付"),
		//****************************
		//结算
		SETTLEMENT("结算"),
		//****************************
		//罚扣
		DEDUCT("罚扣"),
		//反交易
		REVERT("反交易");

		private String descr;

		private BillCode(String descr) {
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
	 * 账单子科目
	 * 
	 * @author DELL
	 *
	 */
	public enum TradeCode implements IEnum {

		//充值-线上
		RECHARGE_ONLINE_WEIXIN("微信充值"),
		RECHARGE_ONLINE_ALIPAY("支付宝充值"),
		RECHARGE_ONLINE_UNION("银联充值"),
        //充值-线下
		RECHARGE_OFFLINE_POSTAL("邮政汇款"),
		RECHARGE_OFFLINE_BANK("银行转账"),
		RECHARGE_OFFLINE_CASH("柜台现金存入"),
		RECHARGE_OFFLINE_POS("POS机"),
		RECHARGE_OFFLINE_PAYPAL("PalPal"),
		RECHARGE_OFFLINE_IPS("IPS"),

		//提现
		WITHDRAW_POSTAL("邮政汇款"),
		WITHDRAW_BANK( "银行转账"),
		WITHDRAW_COUNTER("柜台提现"),
		WITHDRAW_PAYPAL("PayPal"),
		WITHDRAW_COUNTER_NTD("柜台提现(台币)"),
		WITHDRAW_COOPERATIVE_BANK_OF_TAIWAN("台湾合作银行"),

		//支付
		PAY_BALANCE("余额支付"),
		PAY_WEIXIN("微信支付"),
		PAY_ALIPAY("支付宝支付"),
		PAY_UNION("银联支付"),
		//****************************

		//结算
		SETTLEMENT("结算"),
		//****************************

		//罚扣
		DEDUCT_BALANCE("余额罚扣"),

		//反交易
		REVERT("反交易");

		private String descr;

		private TradeCode(String descr) {
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
	 * 退款
	 * 
	 */
	public enum RefundStatus implements IEnum {

		SUCCESS("退款成功"),
		CLOSED("退款关闭");

		private int    value;
		private String name;

		private String descr;

		private RefundStatus(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}


	public enum TradeStatus implements IEnum {

		ONGOING( "交易中"),
		PROCESSING("支付中"),
		CANCEL("交易取消"),
		CLOSED("交易关闭"),
		SUCCESS("交易完成"),
		ERROR("处理失败"),// 因程序异常导致的处理失败, 可以重试.
		EXCEPTION("异常");// 业务异常. 比如一笔转帐/提现操作. 转出帐户金额不足时, 不可重试

		private int    value;
		private String name;

		private String descr;

		private TradeStatus(String descr) {
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
