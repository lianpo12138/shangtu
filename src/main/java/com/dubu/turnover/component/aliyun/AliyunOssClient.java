package com.dubu.turnover.component.aliyun;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.dubu.turnover.utils.UploadForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.File;
import java.io.InputStream;

/**
 * 阿里云Oss上传工具类
 */
@Component
public class AliyunOssClient {

    @Autowired
    AliyunOssConfig aliyunOssConfig;

    private OSSClient client = null;

    @PostConstruct
    public OSSClient init() {
        if (client != null) {
            return client;
        }
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(100);
        conf.setConnectionTimeout(5000);
        conf.setMaxErrorRetry(3);
        conf.setSocketTimeout(2000);
        client = new OSSClient(aliyunOssConfig.getEndpoint(), aliyunOssConfig.getAccessKeyId(), aliyunOssConfig.getAccessKeySecret(), conf);
        return client;
    }

    public String upload(String filename, InputStream inputStream) {
        client.putObject(aliyunOssConfig.getBucketName(), filename, inputStream);
        return aliyunOssConfig.getFileUrl() + "/" + filename;
    }
    public String upload(String filePath,String filename, InputStream inputStream) {
        client.putObject(aliyunOssConfig.getBucketName(), String.format("%s/%s", filePath, filename).replaceAll("/+", "/"), inputStream);
        return aliyunOssConfig.getFileUrl()+"/" + filePath+ "/" + filename;
    }
    public  UploadForm upload(String filePath, String name, File file) {
    	client.putObject(aliyunOssConfig.getBucketName(), String.format("erp/%s/%s", filePath, name).replaceAll("/+", "/"), file);
        //返回图片参数
        UploadForm uploadForm = new UploadForm();
        uploadForm.setFileName(name);
        uploadForm.setFilePath(String.format("/erp/%s/%s", filePath, name).replaceAll("/+", "/"));
        uploadForm.setFileUrl(aliyunOssConfig.getFileUrl());
        uploadForm.setMessage("文件上传成功");
        return uploadForm;
    }
    @PreDestroy
    public void destroy() throws Exception {
        if (client != null) {
            client.shutdown();
        }
    }
}
