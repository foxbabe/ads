package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum SurroundingsDistrictEnum implements EnumMessage<Integer> {

    SchoolDistrict(1,"学校"),
    CommercialDistrict(2,"商业区"),
    ResidentialDistrict(3,"居民区");

    private Integer value;
    private String displayName;

    SurroundingsDistrictEnum(Integer value, String displayName) {
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
