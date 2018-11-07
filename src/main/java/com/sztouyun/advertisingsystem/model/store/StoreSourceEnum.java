package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum StoreSourceEnum  implements EnumMessage<Integer> {

    OMS(1,"运维门店"),
    NEW_OMS(2,"运维新门店");

    private Integer value;
    private String displayName;

    StoreSourceEnum(Integer value,String displayName) {
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
