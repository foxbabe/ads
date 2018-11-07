package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum MaterialLinkActionEnum implements EnumMessage<Integer> {
    load(1, "开始加载"),
    loadComplete(2, "加载完成");

    private Integer value;
    private String displayName;

    MaterialLinkActionEnum(Integer value, String displayName) {
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
