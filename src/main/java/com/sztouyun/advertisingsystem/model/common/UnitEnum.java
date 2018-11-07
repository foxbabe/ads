package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum UnitEnum implements EnumMessage<Integer> {

    Day(1,"天"),
    HOUR(2,"小时"),
    DATE(3,"日"),
    MINUTE(4,"分钟"),

    BILLING_STANDARD(11,"元/广告/门店/天"),
    ECPM_PROFIT_STANDARD(12,"元/千次"),

    Amount(21,"单"),
    Times(51,"次");





    final private Integer value;
    final private String displayName;

    UnitEnum(Integer value,String displayName) {
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
