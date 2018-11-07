package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum TerminalTypeEnum implements EnumMessage<Integer> {

    CashRegister (1,"收银机"),
    IOS(2,"iOS"),
    Android(3,"Android");

    private Integer value;
    private String displayName;

    TerminalTypeEnum(Integer value, String displayName) {
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
