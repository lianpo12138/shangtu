package com.dubu.turnover.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class QQLoginUtils {
    //qq返回信息：access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
    public  static String getAccessToken(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String token = null;
 
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
 
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            if(result.indexOf("access_token") >= 0){
                String[] array = result.split("&");
                for (String str : array){
                    if(str.indexOf("access_token") >= 0){
                        token = str.substring(str.indexOf("=") + 1);
                        break;
                    }
                }
            }
        }
 
        httpGet.releaseConnection();
        return token;
    }
    //qq返回信息：callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} ); 需要用到上面自己定义的解析方法parseJSONP
    public  static String getOpenID(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();
 
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
 
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = parseJSONP(result);
        }
 
        httpGet.releaseConnection();
 
        if(jsonObject != null){
            return jsonObject.getString("openid");
        }else {
            return null;
        }
    }
    private static JSONObject parseJSONP(String jsonp){
        int startIndex = jsonp.indexOf("(");
        int endIndex = jsonp.lastIndexOf(")");
 
        String json = jsonp.substring(startIndex + 1,endIndex);
 
        return JSONObject.parseObject(json);
    }
    public static JSONObject getUserInfo(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();
 
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
 
 
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }
 
        httpGet.releaseConnection();
 
        return jsonObject;
    }
}
