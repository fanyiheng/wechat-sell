package com.example.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "projectUrl")
public class ProjectUrlConfig {
    /**
     * 微信公众平台
     */
    private String wechatMpAuthorize;
    /**
     * 微信开放平台
     */
    private String wechatOpenAuthorize;
    /**
     * 点餐系统
     */
    private String sell;

}
