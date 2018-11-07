package com.sztouyun.advertisingsystem.model.task;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum TaskResultEnum implements EnumMessage<Integer> {
    Resolved(1,"已解决"),
    Unresolved(2,"未解决");

    private Integer value;
    private String displayName;

    TaskResultEnum(Integer value, String displayName) {
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
