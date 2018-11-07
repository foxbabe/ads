package com.sztouyun.advertisingsystem.model.task;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum TaskTypeEnum implements EnumMessage<Integer> {
    Advertisement(1,"广告"),
    Store(2,"门店");

    private Integer value;
    private String displayName;

    TaskTypeEnum(Integer value, String displayName) {
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
