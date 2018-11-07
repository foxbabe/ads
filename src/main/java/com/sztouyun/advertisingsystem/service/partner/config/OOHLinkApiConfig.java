package com.sztouyun.advertisingsystem.service.partner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="partner.oohlink")
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
@Data
public class OOHLinkApiConfig implements  IPartnerConfig{
    private String partnerId;
    private Integer screenSizeWidth;
    private Integer screenSizeHeight;
    private Integer duration;
    private String channelId;
    private String token;
    private String uploadLogUrl;

}
