package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum CommercialAreaEnum implements EnumMessage<Integer> {

    Twenty(20,"0-20"),
    Forty(40,"21-40"),
    Sixty(60,"41-60"),
    Eighty(80,"61-80"),
    Infinity(9999,"81以上");

    private Integer value;
    private String displayName;

    CommercialAreaEnum(Integer value, String displayName) {
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
