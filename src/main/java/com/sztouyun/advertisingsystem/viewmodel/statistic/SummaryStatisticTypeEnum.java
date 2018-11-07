package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum SummaryStatisticTypeEnum implements EnumMessage<Integer> {

    CUSTOMER(1, "客户统计"),
    CONTRACT(2, "合同签约统计"),
    DeliveringAdvertisement(3, "投放中广告统计"),
    AVAILABLE_ADVERTISEMENT_POSITION(4, "可用广告位统计");

    private Integer value;
    private String displayName;

    SummaryStatisticTypeEnum(Integer value, String displayName) {
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
