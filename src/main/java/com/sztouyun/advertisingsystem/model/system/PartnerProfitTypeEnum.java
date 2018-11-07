package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by szty on 2018/9/11.
 */
public enum  PartnerProfitTypeEnum implements EnumMessage<Integer> {
    ECPM(31,"eCPM");
    private Integer value;
    private String displayName;

    PartnerProfitTypeEnum(Integer value, String displayName) {
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
