package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

import java.util.Arrays;

/**
 * 广告位置种类
 */
public enum AdvertisementPositionCategoryEnum implements EnumMessage<Integer> {
    FullScreen(1, "待机全屏广告",TerminalTypeEnum.CashRegister,AdvertisementPositionTypeEnum.FullScreen),
    ScanPay(2, "扫描支付页面",TerminalTypeEnum.CashRegister,AdvertisementPositionTypeEnum.ScanPay),
    SellerFullScreen(3, "商家待机全屏",TerminalTypeEnum.CashRegister,AdvertisementPositionTypeEnum.SellerFullScreen),
    BusinessBanner(4, "商家Banner",TerminalTypeEnum.CashRegister,AdvertisementPositionTypeEnum.BusinessBanner),
    AndroidAppStartingUp(5, "Android - App开屏",TerminalTypeEnum.Android,AdvertisementPositionTypeEnum.AppStartingUp),
    AndroidAppBanner(6, "Android - Banner",TerminalTypeEnum.Android,AdvertisementPositionTypeEnum.Banner),
    IOSAppStartingUp(7, "iOS - App开屏",TerminalTypeEnum.IOS,AdvertisementPositionTypeEnum.AppStartingUp),
    IOSAppBanner(8, "iOS - Banner",TerminalTypeEnum.IOS,AdvertisementPositionTypeEnum.Banner),
    ;

    private Integer value;
    private String displayName;
    private TerminalTypeEnum terminalType;
    private AdvertisementPositionTypeEnum advertisementPositionType;

    AdvertisementPositionCategoryEnum(Integer value, String displayName,TerminalTypeEnum terminalType,AdvertisementPositionTypeEnum advertisementPositionType) {
        this.value = value;
        this.displayName = displayName;
        this.terminalType = terminalType;
        this.advertisementPositionType = advertisementPositionType;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public TerminalTypeEnum getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(TerminalTypeEnum terminalType) {
        this.terminalType = terminalType;
    }

    public AdvertisementPositionTypeEnum getAdvertisementPositionType() {
        return advertisementPositionType;
    }

    public void setAdvertisementPositionType(AdvertisementPositionTypeEnum advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    public static  AdvertisementPositionCategoryEnum getCategoryEnumByPositionAndTerminal(Integer advertisementPositionType ,Integer terminalType){
        return Arrays.stream(AdvertisementPositionCategoryEnum.values()).filter(item->item.getAdvertisementPositionType().getValue().equals(advertisementPositionType) && item.getTerminalType().getValue().equals(terminalType)).findFirst().get();
    }
}
