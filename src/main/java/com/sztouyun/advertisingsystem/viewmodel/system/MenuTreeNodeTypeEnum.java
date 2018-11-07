package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum MenuTreeNodeTypeEnum  implements EnumMessage<Integer> {
    BUTTON(1,"button"),
    MENU(2,"menu");

    private Integer value;
    private String displayName;

    MenuTreeNodeTypeEnum(Integer value,String displayName) {
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
