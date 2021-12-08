package com.dubu.turnover.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class MD5Util {

    private static String byteArrayToHexString(byte b[]) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) resultSb.append(byteToHexString(aB));
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String createSignKey(SortedMap<String, String> headers, String sign) {
        Set<Map.Entry<String, String>> es = headers.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (!StringUtils.isEmpty(key)) {
                sb.append(key).append(value);
            }
        }
        return MD5Encode(sign + sb.toString() + sign, "UTF-8").toUpperCase();
    }
    
	public static String FormatBizQueryParaMap(Map<String, String> paraMap, boolean urlencode) throws Exception {
		String buff = "";
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			for (int i = 0; i < infoIds.size(); i++) {
				Map.Entry<String, String> item = infoIds.get(i);
				//System.out.println(item.getKey());
				if (item.getKey() != "") {
					String key = item.getKey();
					String val = item.getValue();
					if (urlencode) {
						val = URLEncoder.encode(val, "utf-8");
					}
					buff += key + "=" + val + "&";
				}
			}
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return buff;
	}
	
    /**
	 * 获取支付所需签名
	 * @param ticket
	 * @param timeStamp
	 * @param card_id
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getPayCustomSign(Map<String, String> bizObj, String key) throws Exception {
		String bizString = FormatBizQueryParaMap(bizObj, false);
		return sign(bizString, key);
	}
	
	public static String sign(String content, String key) throws Exception {
		String signStr = "";
		signStr = content + "&key=" + key;
		return MD5Encode(signStr, null).toUpperCase();
	}
	
}
