package com.dubu.turnover.domain.enums;

public enum ErrorCode {
	/**
	 * @author yehefeng
	 * @date 2016下午10:11:53
	 *
	 * http://www.dubuinfo.com
	 * 上海笃步
	 *
	 */
	SYS_ERROR("USE_S1001"  , "系统错误" );
	 private String key;
	    private String value;
	    private ErrorCode(String key, String value) {
	        this.key = key;
	        this.value = value;
	    }
	    public static ErrorCode get(String key) {
	        for (ErrorCode pair : values()) {
	            if (pair.key.equals(key)) {
	                return pair;
	            }
	        }
	        return null;
	    }
	    public String getKey() {
	        return key;
	    }
	    public void setKey(String key) {
	        this.key = key;
	    }
	    public String getValue() {
	        return value;
	    }
	    public void setValue(String value) {
	        this.value = value;
	    }
}
