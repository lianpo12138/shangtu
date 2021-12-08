package com.dubu.turnover.domain.enums;

public interface WithdrawsEnum {
	/**
	 * 状态(APPLY:申请 PASS:通过 REJECT:拒绝)
	 */
	public enum Status implements IEnum {
		APPLY("申请"),
		PASS("通过"),
		REJECT("拒绝");

		private String descr;

        private Status(String descr) {
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
	 * 充值类型 POSTAL:邮政转款 BANK:银行转出 COUNTER:上门自取 PAYPAL:PayPal COUNTER_NTD:上门自取(台币) COOPERATIVE_BANK_OF_TAIWAN:台湾合作金库
	 */
	public enum WithdrawType implements IEnum {
		POSTAL("邮政转款"),
		BANK("银行转出"),
		COUNTER("柜台提现"),
		PAYPAL("PayPal"),
		COUNTER_NTD("柜台提现(台币)"),
		COOPERATIVE_BANK_OF_TAIWAN("台湾合作金库");

		private String descr;

        private WithdrawType(String descr) {
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
	 * 提现类型 0:邮政转款 1:银行转出 2:上门自取 3:PayPal 4:上门自取(台币) 5:台湾合作金库
	 */
	public enum RemittanceType {
		POSTAL("邮政汇款"),
		BANK("银行转帐"),
		CASH("现金"),
		POS( "POS机"),
		PAYPAL("IPS"),
		IPS("Paypal");
		private String descr;

        private RemittanceType(String descr) {
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
