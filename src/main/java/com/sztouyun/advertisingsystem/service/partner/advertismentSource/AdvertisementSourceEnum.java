package com.sztouyun.advertisingsystem.service.partner.advertismentSource;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.service.partner.config.BaiDuApiConfig;
import com.sztouyun.advertisingsystem.service.partner.config.IPartnerConfig;
import com.sztouyun.advertisingsystem.service.partner.config.OOHLinkApiConfig;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.SpringUtil;

public enum AdvertisementSourceEnum implements EnumMessage<Integer> {

    BAIDU(1, "百度", BaiDuApiConfig.class,BaiDuAdvertisementService.class),
    OOHLINK(2, "奥凌", OOHLinkApiConfig.class,OOHLinkAdvertisementService.class);

    private Integer value;
    private String displayName;
    private Class<? extends IPartnerConfig> config;
    private Class<? extends IPartnerAdvertisementService> advertisementService;

    AdvertisementSourceEnum(Integer value, String displayName,Class<? extends IPartnerConfig> config,Class<? extends IPartnerAdvertisementService> advertisementService) {
        this.value = value;
        this.displayName = displayName;
        this.config =config;
        this.advertisementService =advertisementService;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public IPartnerAdvertisementService getPartnerAdvertisementService() {
        return SpringUtil.getBean(this.advertisementService);
    }

    public String getPartnerId(){
        return SpringUtil.getBean(config).getPartnerId();
    }

    public static AdvertisementSourceEnum getAdvertisementSource(String partnerId){
        if(org.springframework.util.StringUtils.isEmpty(partnerId))
            return null;
        for (AdvertisementSourceEnum item: EnumUtils.getAllItems(AdvertisementSourceEnum.class)){
            if(partnerId.equals(item.getPartnerId()))
                return item;
        }
        return null;
    }
}