package com.dubu.turnover.component.aliyun;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "config.aliyun.oss")
public class AliyunOssConfig {
    //OSS 服务的地址
    private String endpoint;
    //空间名称
    private String bucketName;
    //访问OSS的Access Key ID
    private String accessKeyId;
    //访问OSS的Access Key Secret
    private String accessKeySecret;
    //返回上传成功外网URL访问域名
    private String fileUrl;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
