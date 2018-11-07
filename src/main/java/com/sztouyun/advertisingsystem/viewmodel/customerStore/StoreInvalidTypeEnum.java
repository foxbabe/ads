package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by szty on 2018/6/15.
 */
public enum StoreInvalidTypeEnum implements EnumMessage<Integer> {
    NotExists(1,"门店不存在"),
    NotAvailable(2,"门店可用广告位数量为0"),
    Duplicate(3,"门店重复"),
    NotDelivery(4,"当前广告未投放此门店"),
    DuplicateDelivery(5,"投放门店重复")
    ;



    private Integer value;
    private String displayName;

    StoreInvalidTypeEnum(Integer value, String displayName) {
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
