package com.sztouyun.advertisingsystem.service.partner.AdvertisementMediation;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.utils.SpringUtil;

public enum AdvertisementMediationEnum implements EnumMessage<Integer> {

    WaterFall(1, "WaterFall",WaterFallAdvertisementMediationService.class),
    FanOut(2, "FanOut",WaterFallAdvertisementMediationService.class),
    Hybrid(3, "Hybrid",WaterFallAdvertisementMediationService.class)
    ;

    private Integer value;
    private String displayName;
    private Class<? extends IAdvertisementMediationService> advertisementMediationService;

    AdvertisementMediationEnum(Integer value, String displayName,Class<? extends IAdvertisementMediationService> advertisementMediationService) {
        this.value = value;
        this.displayName = displayName;
        this.advertisementMediationService = advertisementMediationService;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public IAdvertisementMediationService getAdvertisementMediationService(){
        return SpringUtil.getBean(this.advertisementMediationService);
    }
}