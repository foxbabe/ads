package com.sztouyun.advertisingsystem.model.mongodb;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerMongoLogTypeEnum implements EnumMessage<Integer> {

    UPLOAD_LOG(0, "上报日志"),
    ;

    private Integer value;
    private String displayName;

    PartnerMongoLogTypeEnum(Integer value, String displayName) {
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
