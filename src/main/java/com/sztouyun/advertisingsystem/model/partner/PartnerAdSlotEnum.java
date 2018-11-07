package com.sztouyun.advertisingsystem.model.partner;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;

public enum PartnerAdSlotEnum implements EnumMessage<Integer> {
    FullScreenImg(1, "待机全屏广告 - 图片", AdvertisementPositionCategoryEnum.FullScreen,MaterialTypeEnum.Img),
    FullScreenVideo(2, "待机全屏广告 - 视频",AdvertisementPositionCategoryEnum.FullScreen,MaterialTypeEnum.Video),
    ScanPayImg(3, "扫描支付页面 - 图片",AdvertisementPositionCategoryEnum.ScanPay,MaterialTypeEnum.Img),
    ScanPayVideo(4, "扫描支付页面 - 视频",AdvertisementPositionCategoryEnum.ScanPay,MaterialTypeEnum.Video),
    ;

    private Integer value;
    private String displayName;
    private AdvertisementPositionCategoryEnum advertisementPositionCategory;
    private MaterialTypeEnum materialType;

    PartnerAdSlotEnum(Integer value, String displayName, AdvertisementPositionCategoryEnum advertisementPositionCategory, MaterialTypeEnum materialType) {
        this.value = value;
        this.displayName = displayName;
        this.advertisementPositionCategory = advertisementPositionCategory;
        this.materialType = materialType;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public AdvertisementPositionCategoryEnum getAdvertisementPositionCategory() {
        return advertisementPositionCategory;
    }

    public MaterialTypeEnum getMaterialType() {
        return materialType;
    }
}
