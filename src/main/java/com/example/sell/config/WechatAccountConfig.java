package com.example.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    /**公众平台 appid*/
    private String mpAppId;
    /**公众平台 appSecret*/
    private String mpAppSecret;
    /**开放平台 appid*/
    private String openAppId;
    /**开放平台 appSecret*/
    private String openAppSecret;
    private String mchId;
    private String mchKey;
    private String keyPath;
    private String notifyUrl;
    private Map<String,String> templateIds;
}
