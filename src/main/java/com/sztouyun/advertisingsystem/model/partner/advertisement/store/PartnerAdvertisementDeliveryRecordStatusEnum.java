package com.sztouyun.advertisingsystem.model.partner.advertisement.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerAdvertisementDeliveryRecordStatusEnum implements EnumMessage<Integer> {
    Delivering(1,"投放中"),
    TakeOff (2,"已下架"),
    Finished(3,"已完成");

    private Integer value;
    private String displayName;

    PartnerAdvertisementDeliveryRecordStatusEnum(Integer value, String displayName) {
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
