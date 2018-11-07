package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum HistoricalParamConfigTypeEnum implements EnumMessage<Integer> {
    SHOP_DAYS(1, "广告分成-开店天数",HistoricalParamConfigGroupEnum.AdvertisementProfit),
    BOOT_TIME(2, "广告分成-开机时长",HistoricalParamConfigGroupEnum.AdvertisementProfit),
    ORDERS_MONTHLY_AVERAGE(3, "广告分成-月平均交易订单数量",HistoricalParamConfigGroupEnum.AdvertisementProfit),
    INTO_THE_STANDARD(4, "广告分成-分成标准",HistoricalParamConfigGroupEnum.AdvertisementProfit),
    DAILY_TRADING_ORDER_QUANTITY(5, "广告分成-日交易订单数量",HistoricalParamConfigGroupEnum.AdvertisementProfit),
    FAULT_TOLERANCE(6, "广告分成-容错率",HistoricalParamConfigGroupEnum.AdvertisementProfit),
    SETTLEMENT_DATE(7, "广告结算-结算日期",HistoricalParamConfigGroupEnum.AdvertisementSettlement),
    MESSAGE_REMINDER_DATE(8, "广告结算-消息提醒日期",HistoricalParamConfigGroupEnum.AdvertisementSettlement),
    NOT_PLAY_DAYS(21, "任务配置-未上架天数",HistoricalParamConfigGroupEnum.Task),
    NOT_ACTIVATED_DAYS(22, "任务配置-未激活天数",HistoricalParamConfigGroupEnum.Task),
    EXPECTED_SOLVE_DAYS(23, "任务配置-预计解决天数",HistoricalParamConfigGroupEnum.Task),
    PARTNER_PROFIT_ECPM(31,"eCPM",HistoricalParamConfigGroupEnum.PartnerProfitMode);


    HistoricalParamConfigTypeEnum(int value, String displayName,HistoricalParamConfigGroupEnum group) {
        this.value = value;
        this.displayName = displayName;
        this.group = group;
    }

    private Integer value;
    private String displayName;
    private HistoricalParamConfigGroupEnum group;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public HistoricalParamConfigGroupEnum getGroup() {
        return group;
    }
}
