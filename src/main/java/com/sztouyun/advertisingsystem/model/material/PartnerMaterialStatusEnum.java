package com.sztouyun.advertisingsystem.model.material;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum PartnerMaterialStatusEnum implements EnumMessage<Integer> {
    PendingAuditing(1,"待审核"),
    Approved(2,"审核通过"),
    Rejected(3,"审核失败");

    private Integer value;
    private String displayName;

    PartnerMaterialStatusEnum(Integer value,String displayName) {
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
