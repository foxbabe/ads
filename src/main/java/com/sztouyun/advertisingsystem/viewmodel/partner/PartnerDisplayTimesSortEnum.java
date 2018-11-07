package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerDisplayTimesSortEnum implements EnumMessage<Integer> {
    DISPLAY_TIMES(1, "展示次数"),
    VALID_TIMES(2, "有效次数");

    private Integer value;
    private String displayName;

    PartnerDisplayTimesSortEnum(Integer value, String displayName) {
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
