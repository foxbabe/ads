package com.sztouyun.advertisingsystem.model.customer;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum  CustomerOperationTypeEnum implements EnumMessage<Integer> {
    Distribute(1,"分配"),
    Edit(2,"编辑信息"),
    Delete(3,"删除");

    CustomerOperationTypeEnum(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private Integer value;
    private String displayName;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
