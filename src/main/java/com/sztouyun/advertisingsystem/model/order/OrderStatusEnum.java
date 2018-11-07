package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum OrderStatusEnum implements EnumMessage<Integer> {
    PendingPublishing(1,"待上刊"),
    PublishingAuditing(2,"待上刊审核"),
    PendingDelivery(5,"待投放"),
    Delivering(3,"投放中"),
    PublishingAuditingRejected(4,"审核失败"),
    Canceled(10,"已取消"),
    TakeOff(11,"已下架"),
    Finished(12,"已完成");

    private Integer value;
    private String displayName;

    OrderStatusEnum(Integer value, String displayName) {
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
