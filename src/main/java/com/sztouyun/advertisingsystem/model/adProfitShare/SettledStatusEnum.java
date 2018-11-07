package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum SettledStatusEnum implements EnumMessage<Integer> {
    UnConformed(0,"待确认"),
    AllSettled(1,"全部"),
    UnSettled(2,"待结算"),
    Settled(3,"已结算");

    private Integer value;
    private String displayName;

    SettledStatusEnum(Integer value, String displayName) {
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
