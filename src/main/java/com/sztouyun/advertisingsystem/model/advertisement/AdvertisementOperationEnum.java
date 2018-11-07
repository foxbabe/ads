package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum AdvertisementOperationEnum implements EnumMessage<Integer> {

    Submit(1,"提交审核"),
    Auditing(2,"审核"),
    Delivery(3,"投放"),
    Finish(4, "完成"),
    Edit(5,"编辑");


    private Integer value;
    private String displayName;

    AdvertisementOperationEnum(Integer value, String displayName) {
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