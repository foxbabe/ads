package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum DataDictTypeEnum implements EnumMessage<Integer> {
    Industry(1,"行业")
    ;

    private Integer value;
    private String displayName;


    DataDictTypeEnum(Integer value, String displayName) {
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
