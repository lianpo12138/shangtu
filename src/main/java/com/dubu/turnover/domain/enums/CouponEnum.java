package com.dubu.turnover.domain.enums;

public interface CouponEnum {

	enum ValidType implements IEnum {
		DEADLINE("优惠截止时间固定"), SETTIME("优惠从领取后算固定时间");
		private String descr;

        private ValidType(String descr) {
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
