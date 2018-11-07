package com.sztouyun.advertisingsystem.model.message;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum MessageTypeEnum implements EnumMessage<Integer> {
    Customer(1,"客户"),
    Contract(2,"合同"),
    Advertisement(3,"广告");

    MessageTypeEnum(Integer value,String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private Integer value;
    private String displayName;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
