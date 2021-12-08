package com.dubu.turnover.utils;




import java.text.ParseException;
import java.util.Random;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SmsUtils {
	/**
	 * smsContext 要发送的内容，比如验证码1234
	 * sendPhone  要发送的手机号
	 * tempCode   模板code (验证码的模板:SMS_157278630)
	 */
	public static CommonResponse sendSms(String smsContext, String sendPhone, String tempCode) {

		return null;
	}
	public static String radomString(){
		Random random = new Random();
		String result="";
		for (int i=0;i<6;i++)
		{
			result+=random.nextInt(10);
		}
		return result;
		 }

//	public static void main(String[] args) throws ParseException {
//		
//		Random random = new Random();
//		String result="";
//		for (int i=0;i<6;i++)
//		{
//			result+=random.nextInt(10);
//		}
//		
//		SmsUtils.sendSms(result, "13917637141", "SMS_124015126");
//		System.out.println(SmsUtils.radomString());
//	}
}
