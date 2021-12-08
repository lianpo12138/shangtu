package com.dubu.turnover.utils;

import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by gaoya on 16/6/21.
 */
@Component
public class OkHttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    protected static OkHttpClient client;

    private static String defaultAppId = "1";
    private static String defaultKey = "1";

    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public Headers auth(String method, String path, Integer userId) {
        String appId = defaultAppId;
        String key = defaultKey;
        Headers.Builder builder = new Headers.Builder();
        String strToSign = method + "\n" + "\n" + path + "\n";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            String sign = Base64.encodeBase64String(mac.doFinal(strToSign.getBytes()));
            sign = sign.replaceAll("\r\n","");
            String authorization = "ZHAO " + appId + ":" + sign.trim();
            builder.set("Authorization", authorization);
            builder.set("Content-Type", "application/json;charset=UTF-8");
            builder.set("X-Zhao-AppId", defaultAppId);
            builder.set("X-Zhao-UserId", userId+"");
            logger.info("request:{}|{}|{}|{}|{}", new Object[]{method, path, authorization, defaultAppId, userId});
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        }
        return builder.build();
    }

    public static String doPost(String url, Headers headers, String body) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, body);
        Request post = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();
        Response rsp = client.newCall(post).execute();
        try{
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("post请求结果异常|{}|{}|{}", new Object[]{ url, body, err });
                throw new Exception(err);
            }
            return rsp.body().string();
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }

    public static String doPostReturnBody(String url, Headers headers, String body) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, body);
        Request post = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();
        Response rsp = client.newCall(post).execute();
        try{
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("post请求结果异常|{}|{}|{}", new Object[]{ url, body, err });
                throw new Exception(err);
            }
            return rsp.body().string();
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }

    public static <T> T doGet(String url, Headers headers, Class<T> clazz) throws Exception {
        Request get = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();
        Response rsp = client.newCall(get).execute();
        try{
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("GET请求结果异常|{}|{}", url,err);
                throw new Exception(err);
            }
            return JsonUtils.fromJson(rsp.body().string(), clazz);
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }
    
	public static String doGet(String url, List<BasicNameValuePair> params) throws Exception {
		String requestUrl = attachHttpGetParams(url, params);
		Request get = new Request.Builder().url(requestUrl).get().build();
		Response rsp = client.newCall(get).execute();
		try {
			if (!rsp.isSuccessful()) {
				String err = rsp.body().string();
				logger.info("GET请求结果异常|{}|{}", url, err);
				throw new Exception(err);
			}
			return rsp.body().string();
		} finally {
			if (rsp != null) {
				rsp.close();
			}
		}
	}

    public static <L extends Collection<E>, E> L doGet(String url, Headers headers, Class<E> clazz, Class<L> collection) throws Exception {
        Request get = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();
        Response rsp = client.newCall(get).execute();
        try{
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("GET请求结果异常|{}|{}", url,err);
                throw new Exception(err);
            }
            return JsonUtils.fromJson(rsp.body().string(), collection, clazz);
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }

    public static int doPatch(String url, Headers headers, String body) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, body);
        Request patch = new Request.Builder()
                .url(url)
                .headers(headers)
                .patch(requestBody)
                .build();
        Response rsp = client.newCall(patch).execute();
        try {
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("patch请求结果异常|{}|{}|{}", new Object[]{ url, body,err});
                throw new Exception(err);
            }
            return rsp.code();
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }
    public static int doPatch(String url, Headers headers) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, "");
        Request patch = new Request.Builder()
                .url(url)
                .headers(headers)
                .patch(requestBody)
                .build();
        Response rsp = client.newCall(patch).execute();
        try{
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("patch请求结果异常|{}|{}", new Object[]{ url, err});
                throw new Exception(err);
            }
            return rsp.code();
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }

    public static int doDelete(String url, Headers headers) throws Exception {
        Request delete = new Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build();
        Response rsp = client.newCall(delete).execute();
        try{
            if (!rsp.isSuccessful()) {
                String err = rsp.body().string();
                logger.info("patch请求结果异常|{}|{}", url, err);
                throw new Exception(err);
            }
            return rsp.code();
        }finally {
            if(rsp != null){
                rsp.close();
            }
        }
    }

    /**
     * 这里使用了HttpClinet的API。只是为了方便
     * @param params
     * @return
     */
    public static  String formatParams(List<BasicNameValuePair> params){
        return URLEncodedUtils.format(params, Charset.defaultCharset());
    }
    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     * @param url
     * @param params
     * @return
     */
    public static  String attachHttpGetParams(String url, List<BasicNameValuePair> params){
        return url + "?" + formatParams(params);
    }
    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public  String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }
}
