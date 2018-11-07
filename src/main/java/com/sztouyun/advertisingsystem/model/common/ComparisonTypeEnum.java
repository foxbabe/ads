package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum ComparisonTypeEnum implements EnumMessage<Integer> {
    EQ(1,"="),
    GT(2,">"),
    LT(3,"<"),
    GE(4,">="),
    LE(5,"<=");


    private Integer value;
    private String displayName;

    ComparisonTypeEnum(Integer value, String displayName) {
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
