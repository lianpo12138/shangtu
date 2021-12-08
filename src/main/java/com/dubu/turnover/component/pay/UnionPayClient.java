package com.dubu.turnover.component.pay;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.dubu.turnover.utils.PayUtil;

import net.sf.json.JSONObject;

@Component
public class UnionPayClient {
	private static final Logger logger = Logger.getLogger(UnionPayClient.class);
	
    @Autowired
    private PayConfig payConfig;

	/**
	 * 手机端下单接口
	 */
	public  Map<String, String> moblieOrder(String msgType, String instMid, String tradeType, String merOrderId,
			long totalAmount) {
		// 组织请求报文
		JSONObject json = new JSONObject();
		json.put("mid", payConfig.getMid());
		json.put("tid", payConfig.getTid());
		json.put("msgType", msgType);
		json.put("msgSrc", payConfig.getMsgsrc());
		json.put("instMid", instMid);
		json.put("merOrderId", merOrderId);
		json.put("orderDesc", "商品");
		json.put("totalAmount", totalAmount);
		json.put("tradeType", tradeType);
		json.put("notifyUrl", payConfig.getNotifyurl());
		json.put("returnUrl", payConfig.getReturnurl());

		// 是否要在商户系统下单，看商户需求 createBill()
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		Map<String, String> paramsMap = PayUtil.jsonToMap(json);
		paramsMap.put("sign", PayUtil.makeSign(payConfig.getPublickey(), paramsMap));
		logger.info("paramsMap：" + paramsMap);

		String strReqJsonStr = JSON.toJSONString(paramsMap);
		logger.info("strReqJsonStr:" + strReqJsonStr);

		// 调用银商平台获取二维码接口
		HttpURLConnection httpURLConnection = null;
		BufferedReader in = null;
		PrintWriter out = null;
		// OutputStreamWriter out = null;
		String resultStr = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		if (!StringUtils.isNotBlank(payConfig.getBankurl())) {
			resultMap.put("errCode", "URLFailed");
			return resultMap;
		}

		try {
			URL url = new URL(payConfig.getBankurl());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestProperty("Content_Type", "application/json");
			httpURLConnection.setRequestProperty("Accept_Charset", "UTF-8");
			httpURLConnection.setRequestProperty("contentType", "UTF-8");
			// 发送POST请求参数
			out = new PrintWriter(httpURLConnection.getOutputStream());
			out.write(strReqJsonStr);
			out.flush();

			// 读取响应
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String tempStr = null;
				in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				logger.info("content:" + content.toString());

				// 转换成json对象
				com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
				String resultCode = respJson.getString("errCode");
				resultMap.put("errCode", resultCode);
				resultMap.put("appPayRequest", respJson.getString("appPayRequest"));
				resultMap.put("totalAmount", respJson.getString("totalAmount"));
				resultMap.put("tid", respJson.getString("tid"));
				resultMap.put("mid", respJson.getString("mid"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("errCode", "ERROR");
			resultMap.put("msg", "调用银商接口出现异常：" + e.toString());
			return resultMap;
		} finally {
			if (out != null) {
				out.close();
			}
			httpURLConnection.disconnect();
		}

		logger.info("resultStr:" + resultStr);
		return resultMap;
	}
	/**
	 * 订单状态查询接口
	 */
	public  Map<String, String> searchOrder(String msgType, String instMid,  String merOrderId, String date) {
		// 组织请求报文
		JSONObject json = new JSONObject();
		json.put("msgSrc", payConfig.getMsgsrc());
		json.put("msgType", msgType);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		json.put("mid", payConfig.getMid());
		json.put("tid", payConfig.getTid());
		json.put("instMid", instMid);
		if("bills.query".equals(msgType)){
			json.put("billNo", merOrderId);
			json.put("billDate", date);
		}else{
			json.put("merOrderId", merOrderId);
		}
		Map<String, String> paramsMap = PayUtil.jsonToMap(json);
		paramsMap.put("sign", PayUtil.makeSign(payConfig.getPublickey(), paramsMap));
		System.out.println("paramsMap：" + paramsMap);

		String strReqJsonStr = JSON.toJSONString(paramsMap);
		System.out.println("strReqJsonStr:" + strReqJsonStr);

		// 调用银商平台获取二维码接口
		HttpURLConnection httpURLConnection = null;
		BufferedReader in = null;
		PrintWriter out = null;
		// OutputStreamWriter out = null;
		String resultStr = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		if (!StringUtils.isNotBlank(payConfig.getBankurl())) {
			resultMap.put("errCode", "URLFailed");
			return resultMap;
		}

		try {
			URL url = new URL(payConfig.getBankurl());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestProperty("Content_Type", "application/json");
			httpURLConnection.setRequestProperty("Accept_Charset", "UTF-8");
			httpURLConnection.setRequestProperty("contentType", "UTF-8");
			// 发送POST请求参数
			out = new PrintWriter(httpURLConnection.getOutputStream());
			out.write(strReqJsonStr);
			out.flush();
			// 读取响应
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String tempStr = null;
				in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				logger.info("content:" + content.toString());

				// 转换成json对象
				com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
				if("bills.query".equals(msgType)){
					String billStatus = respJson.getString("billStatus");
					resultMap.put("status", billStatus);
					resultMap.put("totalAmount", respJson.getString("totalAmount"));
				}else{
					String status = respJson.getString("status");
					resultMap.put("status", status);
					resultMap.put("totalAmount", respJson.getString("totalAmount"));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("errCode", "ERROR");
			resultMap.put("msg", "调用银商接口出现异常：" + e.toString());
			return resultMap;
		} finally {
			if (out != null) {
				out.close();
			}
			httpURLConnection.disconnect();
		}

		logger.info("resultStr:" + resultStr);
		return resultMap;
	}
}
