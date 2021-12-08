package com.dubu.turnover.component;

import com.dubu.turnover.domain.UserInfo;

public class ThreadRequestContext {
	private static final ThreadLocal<UserInfo> requestInfoHolder = new ThreadLocal<>();
	public static void set(UserInfo userInfo) {
		requestInfoHolder.set(userInfo);
	}
	public static UserInfo current() {
		return requestInfoHolder.get();
	}
	public static void remove() {
		requestInfoHolder.remove();
	}
}
