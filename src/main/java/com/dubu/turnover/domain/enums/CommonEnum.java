package com.dubu.turnover.domain.enums;



/**
 * 通用枚举类型
 *
 */
public interface CommonEnum {

    /**
     * 状态  VALID 有效   INVALID 无效
     */
    public enum ValidValue implements IEnum {

        VALID("有效"),
        INVALID("禁用");

        private String descr;

        private ValidValue(String descr) {
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
     * YES or NO
     */
    public enum BooleanValue implements IEnum {

        YES("是"),
        NO("否");

        private String descr;

        private BooleanValue(String descr) {
            this.descr = descr;
        }

        public String getName() {
            return super.name();
        }

        public String getDescr() {
            return descr;
        }
    }

    public static enum Application implements IEnum {
        APP("APP"),
        WEB("WEB");

        private Application(String descr) {
            this.descr = descr;
        }

        private String descr;

        public String getDescr() {
            return descr;
        }

        public String getName() {
            return name();
        }
    }

    /**
     * 赵涌在线 web 站点
     *
     * @author dengsuping
     */
    public static enum WebSite implements IEnum {
        ZHAO_WEB("上海站"),
        ZHAO_HOME("用户中心");

        private WebSite(String descr) {
            this.descr = descr;
        }

        private String descr;

        public String getDescr() {
            return descr;
        }

        public String getName() {
            return name();
        }
    }

    enum AppPlatform implements IEnum {
        IOS("IOS", "IOS app"),
        ANDROID("ANDROID", "ANDROID app");

        private String name;

        private String descr;

        AppPlatform(String name, String descr) {
            this.name = name;
            this.descr = descr;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescr() {
            return descr;
        }
    }
	

	public enum DelegationStatus {

		APPLYING(0, "申请中"), ACCEPTED(1, "已受理"), RECEIVING(2, "待收货"), COMPLETED(3, "已完成");

		private int value;
		private String name;

		DelegationStatus(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
	}
}
