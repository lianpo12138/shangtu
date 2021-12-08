package com.dubu.turnover.controller.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Properties;

/**
 * 系统配置
 * @author yehefeng
 * @date 2014下午10:11:53
 *
 * http://www.dubuinfo.com
 * 上海笃步
 *
 */
@Controller
@RequestMapping("/sy/system/sys-property")
public class SysPropertyController {

	@RequestMapping("/input.do")
	public ModelAndView input(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		String fileName="site.properties";
		ServletContext sc = request.getServletContext();
		String filePath = "/WEB-INF/classes/conf/site/".replace("/", File.separator);
		String fileRealPath = sc.getRealPath("/")+filePath+fileName;
		File file=new File(fileRealPath);
		InputStreamReader fr = new InputStreamReader(new FileInputStream(file),"UTF-8");
		
		Properties p = new Properties();  
	    try {
		    p.load(fr);
		    fr.close();

		   String adminEmail = p.getProperty("adminEmail");
		   String adminQQ = p.getProperty("adminQQ");
		   String adminTelephone = p.getProperty("adminTelephone");
		   String icpCode = p.getProperty("icpCode");
		   String tongjiCode = p.getProperty("tongjiCode");
		   String loginBgImg = p.getProperty("loginBgImg");

			modelAndView.addObject("adminEmail", adminEmail);
			modelAndView.addObject("adminQQ", adminQQ);
			modelAndView.addObject("adminTelephone", adminTelephone);
			modelAndView.addObject("icpCode", icpCode);
			modelAndView.addObject("tongjiCode", tongjiCode);
			modelAndView.addObject("loginBgImg", loginBgImg);
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	    modelAndView.setViewName("/diaowen-system/property-input");
	    return modelAndView;
	}

	@RequestMapping("/save.do")
	public ModelAndView save(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		//管理员邮箱
		String adminEmail = request.getParameter("adminEmail");
		//管理员QQ
		String adminQQ = request.getParameter("adminQQ");
		//管理员电话
		String adminTelephone = request.getParameter("adminTelephone");
		//网站备案信息代码
		String icpCode = request.getParameter("icpCode");
		//网站备案信息代码
		String loginBgImg = request.getParameter("loginBgImg");

		String siteFilePath = "/WEB-INF/classes/conf/site/site.properties".replace("/", File.separator);

		Properties props = new Properties();
		props.put("adminEmail",adminEmail!=null?adminEmail:"");
		props.put("adminQQ",adminQQ!=null?adminQQ:"");
		props.put("adminTelephone",adminTelephone!=null?adminTelephone:"");
		props.put("icpCode",icpCode!=null?icpCode:"");
		props.put("loginBgImg",loginBgImg!=null?loginBgImg:"");


		ServletContext sc = request.getServletContext();
		String realPath = sc.getRealPath("/");

		writeData(realPath,siteFilePath,props);

		//写LOGO DATA文件
		String headerData="<a href=\"${ctx }\"><img alt=\"\" src=\"${ctx }/images/logo/LOGO.png\" align=\"middle\" height=\"46\" ><span class=\"titleTempSpan\">OSS</span></a> ";
		String headerDataPath="/WEB-INF/page/layouts/logo-img.jsp".replace("/", File.separator);
		writeData(realPath,headerDataPath, headerData);
		
		if(adminTelephone!=null && adminEmail!=null){
			//写footer文件
			String footer1="<div class=\"dw_footcopy\" style=\"font-size: 16px;color: gray;\"><p style=\"margin-bottom: 6px;\">"
	    	+"邮箱："+adminEmail+"&nbsp;&nbsp;&nbsp;电话："+adminTelephone+"&nbsp;&nbsp;&nbsp;"
	    	+"<a href=\"/\" style=\"color: gray;font-size: 16px;\">"+icpCode+"</a></p></div>";
			String footerPath="/WEB-INF/page/layouts/footer-1.jsp".replace("/", File.separator);
			writeData(realPath,footerPath, footer1);
			
			String adminInfo="<div style=\"color: gray;\"><h3 style=\"line-height: 40px;\">联系信息</h3><p style=\"line-height: 40px;\">邮箱："+adminEmail+"</p><p style=\"line-height: 40px;\">电话："+adminTelephone+"</p><p style=\"line-height: 40px;\">"+icpCode+"</p></div>";
			String adminInfoPath="/WEB-INF/page/layouts/admin-info.jsp".replace("/", File.separator);
			writeData(realPath,adminInfoPath, adminInfo);
		}
		
		if(loginBgImg!=null){
			String loginbgimg="<div class=\"m-logbg\"><img src=\"${ctx }"+loginBgImg+"\" style=\"margin-top:0px; margin-left: 0px; opacity: 1;\" width=\"100%\" ></div>";
			String loginbgimgPath="/WEB-INF/page/layouts/loginbgimg.jsp".replace("/", File.separator);
			writeData(realPath,loginbgimgPath, loginbgimg);
		}
		modelAndView.setViewName("redirect:/sy/system/sys-property/input.do");
		return modelAndView;
	}
	
	
	private void writeData(String realPath, String filePath,String data) {
		OutputStreamWriter fw = null;
		try {
			String fileRealPath = realPath + filePath;
			File file = new File(fileRealPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			fw.write("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\" %>");
			fw.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void writeData(String realPath,String filePath,Properties props) {
		OutputStreamWriter fw = null;
		try {
			String fileRealPath = realPath + filePath;
			File file = new File(fileRealPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			props.store(fw,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String string2Unicode(String string) {

		StringBuffer unicode = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {

			// 取出每一个字符
			char c = string.charAt(i);

			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}

		return unicode.toString();
	}


}
