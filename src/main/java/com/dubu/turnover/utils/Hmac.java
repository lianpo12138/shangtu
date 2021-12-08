package com.dubu.turnover.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Hmac {
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 制作签名，先用HMAC-SHA1(RFC2104)加密，然后再用Base64加密
     *
     * @param key
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String sign(String key, String data) throws NoSuchAlgorithmException,
            InvalidKeyException {
        return Base64.encodeBase64String(Hmac.encode(key, data));
    }

    /**
     * HMAC-SHA1(RFC2104)加密
     *
     * @param key
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encode(String key, String data) throws NoSuchAlgorithmException,
            InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes());
    }
}