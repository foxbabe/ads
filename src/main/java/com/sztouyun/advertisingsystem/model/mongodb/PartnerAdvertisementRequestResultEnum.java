package com.sztouyun.advertisingsystem.model.mongodb;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerAdvertisementRequestResultEnum implements EnumMessage<Integer> {
    GetAd(1,"获取广告"),
    GetNoAd(2,"未获取广告"),
    ApiError(3,"接口异常")
    ;

    private Integer value;
    private String displayName;

    PartnerAdvertisementRequestResultEnum(Integer value, String displayName) {
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
