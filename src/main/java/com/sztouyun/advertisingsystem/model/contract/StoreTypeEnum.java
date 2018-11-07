package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum StoreTypeEnum implements EnumMessage<Integer>{

    A(1,"A类"),
    B(2,"B类"),
    C(3,"C类"),
    D(4, "D类");

    private Integer value;
    private String displayName;

    StoreTypeEnum(Integer value,String displayName) {
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
