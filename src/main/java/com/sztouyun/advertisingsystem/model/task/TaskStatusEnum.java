package com.sztouyun.advertisingsystem.model.task;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum TaskStatusEnum implements EnumMessage<Integer> {
    PendingDistribute(1,"待派发"),
    OnGoing(2,"进行中"),
    Finished(3,"已完成"),
    Cancel(4,"已取消");

    private Integer value;
    private String displayName;

    TaskStatusEnum(Integer value, String displayName) {
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
