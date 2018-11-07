package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum MonitorStatusEnum implements EnumMessage<Integer> {
    All(0,"全部"),
    OnWatching(1,"监控中"),
    Finished(2,"已完成");


    private Integer value;
    private String displayName;

    MonitorStatusEnum(Integer value, String displayName) {
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
