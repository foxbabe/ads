package com.sztouyun.advertisingsystem.model.material;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerMaterialOperationEnum implements EnumMessage<Integer> {
    Submit(1,"提交审核"),
    Auditing(2,"审核");

    private Integer value;
    private String displayName;

    PartnerMaterialOperationEnum(Integer value, String displayName) {
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
