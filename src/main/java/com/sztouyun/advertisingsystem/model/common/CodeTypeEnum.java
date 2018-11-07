package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum CodeTypeEnum implements EnumMessage<Integer> {

    CTX(1,"合同编码"),
    BTE(2,"客户编码"),
    CAB(3,"合作意向"),
    BTY(4,"合作协议"),
    ORG(5,"组织机构"),
    PAR(6,"合作方编码"),
    ORDER(7,"广告订单编码"),
    PMTR(8,"第三方素材编码"),
    DHC(9,"任务编码"),
    CSP(10,"广告客户选点记录"),

    ;

    private Integer value;
    private String displayName;

    CodeTypeEnum(Integer value,String displayName) {
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
