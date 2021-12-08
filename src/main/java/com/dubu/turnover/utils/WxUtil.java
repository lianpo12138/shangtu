/**
 *
 */
package com.dubu.turnover.utils;

import java.util.Formatter;
import java.util.UUID;

/**
 * @author
 */
public class WxUtil {

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 获取create_nonce_str（uuid）
     *
     * @return
     */
    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取create_timestamp（时间）
     *
     * @return
     */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
