package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by szty on 2018/7/25.
 */
public enum RequestStoreRankSortTypeEnum implements EnumMessage<Integer> {
    Default(1,"城市数量"),
    ValidRequestCount(2,"有效请求次数"),
    ValidRatio(3,"有效占比")
    ;

    private Integer value;
    private String  displayName;

    RequestStoreRankSortTypeEnum(Integer value, String displayName) {
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
