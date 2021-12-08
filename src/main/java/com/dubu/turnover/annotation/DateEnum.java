package com.dubu.turnover.annotation;

public enum DateEnum {

    /** yyyy-MM-dd */
    DATE("yyyy-MM-dd"),

    /** yyyy-MM */
    DATE_MONTH("yyyy-MM"),

    /** yyyyMMdd */
    UNSIGNED_DATE("yyyyMMdd"),

    /** yyyy/MM/dd */
    DIR_DATE("yyyy/MM/dd"),
    /** HHmmss */
    NON_SEP_TIME("HHmmss"),

    /** HH:mm:ss */
    TIME("HH:mm:ss"),

    /** yyyy-MM-dd HH:mm:ss */
    DATETIME("yyyy-MM-dd HH:mm:ss"),

    /** yyyyMMddHH:mm:ss */
    UNSIGNED_DATETIME("yyyyMMddHHmmss"),
    /**yyyy-MM-dd'T'HH:mm:ss'Z'*/
    DATETIMEUTC("yyyy-MM-dd'T'HH:mm:ss'Z'"),

    SIGNED_DATETIME("yyyyMMddHHmmssSSS"),

    FULL_DATE_PATTERN("yyyy-MM-dd HH:mm:ss"),

    PORTAL_DATE_PATTERN("yyyy/MM/dd HH:mm:ss"),

    MONTH_AND_DAY("MM-dd");

    private String value;

    private DateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
