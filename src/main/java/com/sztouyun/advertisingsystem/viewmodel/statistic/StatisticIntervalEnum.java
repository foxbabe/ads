package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum StatisticIntervalEnum implements EnumMessage<Integer> {

    PAST_SEVEN_DAYS(7, "过去7天"),
    PAST_TWO_WEEKS(14, "过去两周"),
    PAST_MONTH(30, "过去一个月");

    private Integer value;
    private String displayName;

    StatisticIntervalEnum(Integer value, String displayName) {
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