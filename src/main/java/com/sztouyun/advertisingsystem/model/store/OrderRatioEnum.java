package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum OrderRatioEnum implements EnumMessage<Integer> {

    Twenty(20,"20%以下"),
    thirtyNine(39,"21%-39%"),
    FiftyNine(59,"40%-59%"),
    SeventyNine(79,"60%-79%"),
    NinetyNine(99,"80%-99%"),
    Hundred(100,"100%");

    private Integer value;
    private String displayName;

    OrderRatioEnum(Integer value, String displayName) {
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
