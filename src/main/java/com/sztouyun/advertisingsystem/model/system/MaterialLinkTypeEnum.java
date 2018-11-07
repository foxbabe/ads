package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum MaterialLinkTypeEnum implements EnumMessage<Integer> {
    MaterialClick(1, "Url点击"),
    MaterialQRCode(2, "二维码");

    private Integer value;
    private String displayName;

    MaterialLinkTypeEnum(Integer value, String displayName) {
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
