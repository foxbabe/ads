package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum HistoricalParamConfigGroupEnum implements EnumMessage<Integer> {
    AdvertisementProfit(1, "广告分成"),
    AdvertisementSettlement(2, "广告结算"),
    Task(3, "任务"),
    PartnerProfitMode(4,"合作方计费模式")
    ;

    HistoricalParamConfigGroupEnum(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private Integer value;
    private String displayName;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
