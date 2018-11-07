package com.sztouyun.advertisingsystem.viewmodel.index;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum DistributionStatisticTypeEnum implements EnumMessage<Integer> {
    Customer(1,"客户区域分布"),
    Contract(2,"合同状态组成"),
    Advertisement(3,"广告状态组成"),
    AdvertisementPosition(4,"可用广告位组成");








    private Integer value;
    private String displayName;

    DistributionStatisticTypeEnum(Integer value, String displayName) {
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
