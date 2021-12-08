package com.dubu.turnover.core;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一API响应结果封装
 */
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public Result setCode(int resultCode) {
        this.code = resultCode;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("unchecked")
    public Result putData(String key, Object value) {
        if (null == this.data) {
            this.data = new HashMap<String, Object>();
        }
        if (this.data instanceof Map) {
            Map<String, Object> map = ((Map) this.data);
            map.put(key, value);
        }
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
