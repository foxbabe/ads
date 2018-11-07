package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum QRCodePositionEnum implements EnumMessage<Integer> {
    LowerLeftCorner(1, "左下角"),
    LowerRightCorner(2, "右下角"),
    Center(3, "居中");

    private Integer value;
    private String displayName;

    QRCodePositionEnum(Integer value, String displayName) {
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
