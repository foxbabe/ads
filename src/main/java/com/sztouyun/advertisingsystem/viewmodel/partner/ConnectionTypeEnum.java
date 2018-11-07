package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum ConnectionTypeEnum implements EnumMessage<Integer> {

    UNKNOWN_NETWORK(0, "未知网络"),
    WIFI(1, "wifi"),
    MOBILE_2G(2, "2G"),
    MOBILE_3G(3, "3G"),
    MOBILE_4G(4, "4G"),
    ETHERNET(101, "以太网接入"),
    NEW_TYPE(999, "未知新类型")
    ;

    private Integer value;
    private String displayName;

    ConnectionTypeEnum(Integer value, String displayName) {
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
