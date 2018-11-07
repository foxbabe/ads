package com.sztouyun.advertisingsystem.service.partner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="partner.baidu")
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
@Data
public class BaiDuApiConfig implements IPartnerConfig {
    private String appId;
    private String partnerId;
    private Integer screenSizeWidth;
    private Integer screenSizeHeight;
    private String fullScreenImgAdsLotId;
    private String fullScreenVideoAdsLotId;
    private String scanPayImgAdsLotId;
    private String scanPayVideoAdsLotId;
    private Integer duration;
}
