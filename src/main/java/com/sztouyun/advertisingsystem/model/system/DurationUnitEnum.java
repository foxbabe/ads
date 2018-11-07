package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;


public enum DurationUnitEnum implements EnumMessage<Integer>{

    H(1,"时"),
    M(2,"分"),
    S(3,"秒");

    private Integer value;
    private String displayName;

    DurationUnitEnum(Integer value,String displayName) {
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