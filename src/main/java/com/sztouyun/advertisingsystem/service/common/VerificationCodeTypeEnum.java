package com.sztouyun.advertisingsystem.service.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum VerificationCodeTypeEnum implements EnumMessage<Integer> {
    ResetPassword(1,"重置密码");

    private Integer value;
    private String displayName;

    VerificationCodeTypeEnum(Integer value, String displayName) {
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
