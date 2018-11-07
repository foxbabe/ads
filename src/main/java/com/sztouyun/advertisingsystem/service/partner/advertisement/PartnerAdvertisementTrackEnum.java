package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerAdvertisementTrackEnum implements EnumMessage<Integer> {

    StartPlay(1, "开始播放"),
    EndPlay(2, "结束播放");

    private Integer value;
    private String displayName;

    PartnerAdvertisementTrackEnum(Integer value, String displayName) {
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