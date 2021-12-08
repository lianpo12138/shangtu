package com.dubu.turnover.controller.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dubu.turnover.component.aliyun.AliyunOssClient;
import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.utils.MD5Util;


/**
 * 公共操作
 */
@RestController
@RequestMapping(value = "/common")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private AliyunOssClient aliyunOssClient;
    @Autowired
    private Environment environment;
    
    @Autowired
    SysConfig sysConfig;
    
    
    @PostMapping(value = "/uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file,String fileType) throws IOException {
    	try{
            Long size = file.getSize();
            if (size > 20*1024*1024){
                return ResultGenerator.error("图片超过20M");
            }
	    	String filePath=sysConfig.getFileUrl();
            String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
	    	String newFileName=java.util.UUID.randomUUID().toString()+"."+prefix;
			String path ="surveytest" +File.separator+java.util.UUID.randomUUID().toString()+File.separator;
	        File targetFile = new File(filePath+path);  
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();
	        }  
	        //保存  
	        File targetFile2 = new File(filePath+path+newFileName);  
	        file.transferTo(targetFile2); 
    	    
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", sysConfig.getShowUrl()+File.separator+path+newFileName);
            map.put("fileType", file.getContentType().substring(0,file.getContentType().indexOf("/")));
            map.put("fileName", file.getOriginalFilename());
            return ResultGenerator.success(map);
    	}catch(Exception e){
    		return ResultGenerator.error(e.getMessage());
    	}

    }
}
