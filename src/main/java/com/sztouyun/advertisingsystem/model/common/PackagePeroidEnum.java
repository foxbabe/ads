package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by szty on 2017/7/28.
 */
public enum PackagePeroidEnum implements EnumMessage<Integer> {
    One(1,"1个月"),
    Two(2,"2个月"),
    Three(3,"3个月"),
    Six(6,"6个月"),
    Twelve(12,"12个月");

    private Integer value;
    private String displayName;

    PackagePeroidEnum(Integer value, String displayName) {
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
