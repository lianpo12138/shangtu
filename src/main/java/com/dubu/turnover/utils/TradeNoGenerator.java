package com.dubu.turnover.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class TradeNoGenerator {

    private static final Log LOG = LogFactory.getLog(TradeNoGenerator.class);
	private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	private static       AtomicInteger     seq = new AtomicInteger(ThreadLocalRandom.current().nextInt(9998));
	private static String suffix;

	static {

		try {
			String ipStr = Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
			                          .flatMap(i -> Collections.list(i.getInetAddresses()).stream())
			                          .filter(ip -> ip instanceof Inet4Address && ip.isSiteLocalAddress())
			                          .findFirst().orElseThrow(RuntimeException::new)
			                          .getHostAddress();
			String[] ipAddressInArray = ipStr.split("\\.");
			long ip = 0;
			for (int i = 3; i >= 0; i--) {
				long part = Long.parseLong(ipAddressInArray[3 - i]);
				// left shifting 24,16,8,0 and bitwise OR
				// 1. 192 << 24
				// 1. 168 << 16
				// 1. 1 << 8
				// 1. 2 << 0
				ip |= part << (i * 8);
			}
			String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			suffix = String.format("%05d%010d8", Integer.valueOf(pid), ip);
		} catch (SocketException e) {
			LOG.error("", e);
			String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			suffix = String.format("%05d%010d8", Integer.valueOf(pid), 0);
		}
	}

	public static String generate() {
		seq.compareAndSet(9999, ThreadLocalRandom.current().nextInt(9998));
		return String.format("%s%s%04d", new DateTime().toString(fmt), suffix, seq.getAndIncrement());
	}
}
