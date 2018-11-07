package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.service.partner.AdvertisementMediation.AdvertisementMediationEnum;

public enum SystemParamTypeEnum implements EnumMessage<Integer> {
    TerminalType(1,"终端类型",TerminalTypeEnum.class),
    AdvertisementPositionType(2,"广告位置",AdvertisementPositionTypeEnum.class),
    MaterialLinkType(3, "素材链接", MaterialLinkTypeEnum.class),
    AdvertisementMediationEnum(4, "广告投放策略", AdvertisementMediationEnum.class),
    ;

    private Integer value;
    private String displayName;
    private Class<? extends EnumMessage<Integer>>  valueEnum;

    SystemParamTypeEnum(Integer value, String displayName, Class<? extends EnumMessage<Integer>> valueEnum) {
        this.value = value;
        this.displayName = displayName;
        this.valueEnum = valueEnum;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }


    public Class<? extends EnumMessage<Integer>> getValueEnum() {
        return valueEnum;
    }
}
