package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;


public enum GradientParamConfigEnum implements EnumMessage<Integer>{

    StoreOrderCount(1,"门店订单数量"),
    StoreBootTime(2,"门店开机时长"),
    ;


    private Integer value;
    private String displayName;

    GradientParamConfigEnum(Integer value, String displayName) {
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