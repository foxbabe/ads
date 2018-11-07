package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum OrderOperationEnum implements EnumMessage<Integer> {
    SubmitDeliveryAuditing(1,"提交投放审核"),
    DeliveryAuditing(2,"投放审核"),
    AutoDelivery(3,"自动投放"),
    Cancel(10,"取消订单"),
    TakeOff(11,"下架订单"),
    Finish(12,"完成订单"),
    Create(13,"新建订单")
    ;

    private Integer value;
    private String displayName;

    OrderOperationEnum(Integer value, String displayName) {
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
