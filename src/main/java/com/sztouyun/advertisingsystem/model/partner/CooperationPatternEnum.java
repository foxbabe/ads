package com.sztouyun.advertisingsystem.model.partner;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum CooperationPatternEnum implements EnumMessage<Integer> {

    RTB(1, "RTB模式"),
    GA(2, "GA合约模式");

    private Integer value;
    private String displayName;

    CooperationPatternEnum(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }
}
