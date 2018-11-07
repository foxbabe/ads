package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum AdvertisementMaterialUrlParamEnum implements EnumMessage<Integer> {

    Phone(1,"手机号"),
    WeChatID(2,"微信ID"),
    IDCard(3,"身份证号");

    private Integer value;
    private String displayName;

    AdvertisementMaterialUrlParamEnum(Integer value, String displayName) {
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