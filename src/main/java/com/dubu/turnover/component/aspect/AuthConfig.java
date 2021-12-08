package com.dubu.turnover.component.aspect;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "config")
public class AuthConfig {
    // 用户中心dubbo默认appid
    private String defaultAppId;

    private String defaultAppKey;

    public String getDefaultAppId() {
        return defaultAppId;
    }

    public void setDefaultAppId(String defaultAppId) {
        this.defaultAppId = defaultAppId;
    }

    public String getDefaultAppKey() {
        return defaultAppKey;
    }

    public void setDefaultAppKey(String defaultAppKey) {
        this.defaultAppKey = defaultAppKey;
    }
}
