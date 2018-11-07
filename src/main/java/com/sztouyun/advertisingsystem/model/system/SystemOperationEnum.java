package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum SystemOperationEnum implements EnumMessage<Integer> {

    TerminalAndAdvertisementPosition(1,"终端类型和广告位配置"),
    TaskConfig(2,"任务配置"),
    ;
    private Integer value;
    private String displayName;

    SystemOperationEnum(Integer value, String displayName) {
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
