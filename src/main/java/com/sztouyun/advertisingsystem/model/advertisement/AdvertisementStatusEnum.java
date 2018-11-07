package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum AdvertisementStatusEnum implements EnumMessage<Integer> {
    PendingCommit(1,"待提交"),
    PendingAuditing(2,"待审核"),
    PendingDelivery(3,"待投放"),
    Delivering(4,"投放中"),
    TakeOff (5,"已下架"),
    Finished(6,"已完成");









    private Integer value;
    private String displayName;

    AdvertisementStatusEnum(Integer value, String displayName) {
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
