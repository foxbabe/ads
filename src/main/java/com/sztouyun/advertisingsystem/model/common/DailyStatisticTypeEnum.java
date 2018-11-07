package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by szty on 2018/9/5.
 */
public enum DailyStatisticTypeEnum  implements EnumMessage<Integer> {
    CustomerCount(1,"客户数量");
    private Integer value;
    private String displayName;

    DailyStatisticTypeEnum(Integer value, String displayName) {
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
