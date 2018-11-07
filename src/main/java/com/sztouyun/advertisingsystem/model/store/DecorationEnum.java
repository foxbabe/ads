package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum DecorationEnum implements EnumMessage<Integer> {

    Ultrafine(1,"精超"),
    BasicDecoration(2,"基本装修"),
    Unfurnished(3,"无装修"),
    QuanJia(4,"全家"),
    LianHua(5,"联华"),
    VarietyStore(6,"杂货店"),
    ElseDecoration(100,"其他");

    private Integer value;
    private String displayName;

    DecorationEnum(Integer value, String displayName) {
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
