package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by wenfeng on 2018/3/21.
 */
public enum StorePictureTypeEnum implements EnumMessage<Integer> {
    OutsidePicture(4,"门店照"),
    InsidePicture(5,"店内照"),
    CashRegisterPicture(7,"收银台照");
    private Integer value;
    private String displayName;

    StorePictureTypeEnum(Integer value, String displayName) {
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
