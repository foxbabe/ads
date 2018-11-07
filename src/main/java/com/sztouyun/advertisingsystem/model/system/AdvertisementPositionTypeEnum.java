package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by wenfeng on 2017/10/17.
 */
public enum AdvertisementPositionTypeEnum implements EnumMessage<Integer> {
    FullScreen(1, "待机全屏广告"),
    ScanPay(2, "扫描支付页面"),
    SellerFullScreen(3, "商家待机全屏"),
    AppStartingUp(4, "App开屏"),
    Banner(5, "Banner"),
    BusinessBanner(6, "商家Banner");

    private Integer value;
    private String displayName;

    AdvertisementPositionTypeEnum(Integer value, String displayName) {
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
