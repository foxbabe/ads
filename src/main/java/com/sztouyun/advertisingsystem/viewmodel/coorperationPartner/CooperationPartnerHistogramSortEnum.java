package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum CooperationPartnerHistogramSortEnum implements EnumMessage<Integer> {

    RequestTimes(1, "请求次数"),
    RequestSuccessTimes(2, "请求成功次数");

    private Integer value;
    private String displayName;

    CooperationPartnerHistogramSortEnum(Integer value, String displayName) {
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
