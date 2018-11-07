package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum AdvertisementMaterialUrlStepTypeEnum implements EnumMessage<Integer> {

    Promotion(1,"推广页面"),
    FillPhone(2,"填写手机号页面")
    ;

    private Integer value;
    private String displayName;

    AdvertisementMaterialUrlStepTypeEnum(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}