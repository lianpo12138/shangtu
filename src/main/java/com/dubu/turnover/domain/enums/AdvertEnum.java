package com.dubu.turnover.domain.enums;



/**
 * 广告枚举类型
 * 
 *
 */
public interface AdvertEnum {

	/**
	 * 链接类型 WEB_VIEW, NATIVE_APP, PC_PAGE, MSG_CENTER
	 * 
	 * @author DELL
	 *
	 */
	public enum LinkType implements IEnum {

		WEB_VIEW("WEB VIEW"), NATIVE_APP("原生 APP");

		private String descr;

		private LinkType(String descr) {
			this.descr = descr;
		}

		public String getName() {
			return super.name();
		}

		public String getDescr() {
			return descr;
		}
	}

	public static enum HeadType implements IEnum {
		H5("H5 头区域"), NATIVE_APP("原生APP头区域");

		private String descr;

		private HeadType(String descr) {
			this.descr = descr;
		}

		public String getDescr() {
			return descr;
		}

		public String getName() {
			return name();
		}
	}

	public static enum Platform implements IEnum {
		ALL("全部"), ANDROID("ANDROID"), IOS("IOS"), H5("H5"), ANDROID_IOS("ANDROID 和 IOS");

		private String descr;

		private Platform(String descr) {
			this.descr = descr;
		}

		public String getDescr() {
			return descr;
		}

		public String getName() {
			return name();
		}

		public String[] getPlatforms(Platform platform) {
			switch (platform) {
			case ANDROID:
				return new String[] { ALL.name(), ANDROID.name(), ANDROID_IOS.name() };
			case IOS:
				return new String[] { ALL.name(), IOS.name(), ANDROID_IOS.name() };
			case H5:
				return new String[] { ALL.name(), H5.name() };
			case ANDROID_IOS:
				return new String[] { ANDROID_IOS.name() };
			default:
				return new String[] { ALL.name() };
			}
		}
	}

	public static enum ShowType implements IEnum {
		NORMAL_DIV("正常显示"), POPUP("弹窗"), FULL_SCREEN("全屏");

		private String descr;

		private ShowType(String descr) {
			this.descr = descr;
		}

		public String getDescr() {
			return descr;
		}

		public String getName() {
			return name();
		}
		
		public String[] getShowTypes(ShowType showtype) {
			switch (showtype) {
			case NORMAL_DIV:
				return new String[] { NORMAL_DIV.name(), POPUP.name(), FULL_SCREEN.name() };
			case POPUP:
				return new String[] { POPUP.name() };
			case FULL_SCREEN:
				return new String[] { FULL_SCREEN.name() };
			default:
				return new String[] { NORMAL_DIV.name(), POPUP.name(), FULL_SCREEN.name()};
			}
		}
	}

	public static enum RepeatType implements IEnum {
		ONCE("仅显示一次"), EVERYDAY("每天显示一次"),ALWAYS("一直显示");

		private String descr;

		private RepeatType(String descr) {
			this.descr = descr;
		}

		public String getDescr() {
			return descr;
		}

		public String getName() {
			return name();
		}

	}

	

	public static enum Category implements IEnum {
		STAMP(100,"邮票"), 
		STAMP__QINGMIN_DISTRICT(101,"邮票/清明区邮票"), 
		STAMP__NEW_CHINA(143,"邮票/新中国邮票"),
		STAMP__PHILATELIC_COVERS(154,"邮票/封片简"),
		STAMP__FOREIGN(470,"邮票/外国邮票"),
		STAMP__HK_MAC(164,"邮票/港澳邮票"),
		STAMP__OTHERS(165,"邮票/其他邮票"),
		COIN(157,"钱币"),
		COIN__CHINESE_CURRENCY_COMMEMORATIVE(191,"钱币/中国流通纪念币分类"), 
		COIN__CHINA_MODERN_MECHANISM_CURRENCY(184,"钱币/中国近代机制币分类"), 
		COIN__COPPER_CHAPTER(188,"钱币/铜章分类"), 
		COIN__SILVER_INGOT(510,"钱币/银锭分类"), 
		COIN__ANCIENT(500,"钱币/古钱分类"), 
		COIN__CHINESE_MODERN_GOLD_SILVER(183,"钱币/中国现代金银币分类"), 
		COIN__FOREIGN_COMMEMORATIVE(1851,"钱币/外国纪念币分类"),
		COIN__FOREIGN_INVESTMENT(1852,"钱币/外国投资币分类"),
		COIN__QINGMIN_DYNASTIES_NOTES(710,"钱币/明清纸钞分类"), 
		COIN__REPUBLIC_OF_CHINA_NOTES(720,"钱币/民国纸钞分类"), 
		COIN__NEW_CHINA_NOTES(730,"钱币/新中国纸钞分类"), 
		COIN__STOCK_BOND(770,"钱币/股票债券分类"), 
		COIN__HK_BANKNOTES(740,"钱币/香港纸钞分类"), 
		COIN__MACAO_BANKNOTES(750,"钱币/澳门纸钞分类"), 
		COIN__TAIWAN_BANKNOTES(760,"钱币/台湾纸钞分类"), 
		COIN__FOREIGN_BANKNOTES(187,"钱币/外国纸钞分类"), 
		COIN__OTHERS(182,"钱币/其他钱币分类");
		
		private Integer id;
		private String descr;

		private Category(Integer id,String descr) {
			this.id = id;
			this.descr = descr;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDescr() {
			return descr;
		}

		public void setDescr(String descr) {
			this.descr = descr;
		}
		public static Category getCategoryById(Integer id) {
			Category category=null;
			for (Category temp : Category.values()) {
				if (temp.id.equals(id)) {
					category = temp;
					break;
				}
			}
			return category;
		}
		@Override
		public String getName() {
			return name();
		}
	}
}
