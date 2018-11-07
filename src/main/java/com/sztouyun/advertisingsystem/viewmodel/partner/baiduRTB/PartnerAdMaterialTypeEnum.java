package com.sztouyun.advertisingsystem.viewmodel.partner.baiduRTB;


import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;

public enum  PartnerAdMaterialTypeEnum implements EnumMessage<Integer> {

    VIDEO(1, "VIDEO", MaterialTypeEnum.Video),
    IMAGE(2, "IMAGE",MaterialTypeEnum.Img),
            ;

    private Integer value;
    private String displayName;
    private MaterialTypeEnum materialTypeEnum;

    PartnerAdMaterialTypeEnum(Integer value, String displayName, MaterialTypeEnum materialTypeEnum) {
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
