package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;


import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;

public enum OOHLinkAdMaterialTypeEnum implements EnumMessage<Integer> {

    IMAGE(1, "IMAGE",MaterialTypeEnum.Img),
    VIDEO(2, "VIDEO", MaterialTypeEnum.Video),
            ;

    private Integer value;
    private String displayName;
    private MaterialTypeEnum materialTypeEnum;

    OOHLinkAdMaterialTypeEnum(Integer value, String displayName, MaterialTypeEnum materialTypeEnum) {
        this.value = value;
        this.displayName = displayName;
        this.materialTypeEnum = materialTypeEnum;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public MaterialTypeEnum getPartnerMaterialType() {
        return materialTypeEnum;
    }
}
