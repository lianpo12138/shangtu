package com.dubu.turnover.utils;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dubu.turnover.component.config.SysConfig;
 
@Component
public class EmailUtil {
	
	private static  SysConfig sysConfig;
	
	@Autowired
	public void init(SysConfig sysConfig) {
		EmailUtil.sysConfig = sysConfig;
	}
    
	public static void send_email(String to,String title,String context) {
	 
	      // 指定发送邮件的主机为 smtp.qq.com
	      String host = sysConfig.getHost();  //QQ 邮件服务器
	      String sendMail=sysConfig.getSendMail();
	      String userName=sysConfig.getUserName();
	      String password=sysConfig.getPassword();
	      String port=sysConfig.getPort();
	 
	      // 获取系统属性
	      Properties properties = System.getProperties();
	 
	      // 设置邮件服务器
	      properties.setProperty("mail.smtp.host", host);
	      properties.setProperty("mail.smtp.port", port);
	      properties.put("mail.smtp.auth", "true");
	      properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	      properties.setProperty("mail.smtp.ssl.enable", "false");// 开启ssl
	      // 获取默认session对象
	      Session session = Session.getInstance(properties, new Authenticator(){
	          protected PasswordAuthentication getPasswordAuthentication() {
	              return new PasswordAuthentication(userName, password);
                        //第一个参数是发者的邮箱  第二个参数是你的授权码  这一步我已经改过不会出现错误
	          }});
	 
	      try{
	         // 创建默认的 MimeMessage 对象
	         MimeMessage message = new MimeMessage(session);
	 
	         // Set From: 头部头字段
	         message.setFrom(new InternetAddress(sendMail));
	 
	         // Set To: 头部头字段
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	 
		      // Set Subject: 头部头字段
		     message.setSubject(title);
	         //message.setText(context);
			 message.setContent(context, "text/html; charset=GB2312");
	   
	 
	         // 发送消息
	         Transport.send(message);
	         System.out.println("发送成功");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	public static void main(String[] args)  {
	
	Random random = new Random();
	String result="";
	for (int i=0;i<6;i++)
	{
		result+=random.nextInt(10);
	}
	
	//EmailUtil.send_email("363938720@qq.com", result,"1","https://iscommunitytest.ipsos.cn/wenjuanvue/#/forgotpwd");
	System.out.println(SmsUtils.radomString());
}
}
