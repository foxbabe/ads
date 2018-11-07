package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum DisplayTimeUnitEnum implements EnumMessage<Integer> {
    Day(1,"天"),
    Week(2,"周");


    private Integer value;
    private String displayName;

    DisplayTimeUnitEnum(Integer value, String displayName) {
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
