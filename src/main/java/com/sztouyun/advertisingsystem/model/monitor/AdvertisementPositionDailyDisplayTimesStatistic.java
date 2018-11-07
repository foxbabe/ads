package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import lombok.Data;

import java.util.Date;

@Data
public class AdvertisementPositionDailyDisplayTimesStatistic {
    /**
     * 日期
     */
    private Date date;

    /**
     * 门店数量
     */
    private Long storeNum;

    /**
     * 展示次数 - 待机全屏广告 {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory1DisplayTimes;

    /**
     * 展示次数 - 扫描支付页面 {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory2DisplayTimes;

    /**
     * 展示次数 - 商家待机全屏 {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory3DisplayTimes;

    /**
     * 展示次数 - 商家Banner {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory4DisplayTimes;

    /**
     * 展示次数 - Android - App开屏 {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory5DisplayTimes;

    /**
     * 展示次数 - Android - Banner {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory6DisplayTimes;

    /**
     * 展示次数 - iOS - App开屏 {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory7DisplayTimes;

    /**
     * 展示次数 - iOS - Banner {@link AdvertisementPositionCategoryEnum}
     */
    private Long advertisementPositionCategory8DisplayTimes;

    /**
     * 当前日期的总展示次数
     */
    public Long getTotalDisplayTimes() {
        return advertisementPositionCategory1DisplayTimes + advertisementPositionCategory2DisplayTimes + advertisementPositionCategory3DisplayTimes + advertisementPositionCategory4DisplayTimes +
                advertisementPositionCategory5DisplayTimes + advertisementPositionCategory6DisplayTimes + advertisementPositionCategory7DisplayTimes + advertisementPositionCategory8DisplayTimes;
    }

    /**
     * 当前日期的收银机展示次数
     */
    public Long getCashRegisterDisplayTimes() {
        return advertisementPositionCategory1DisplayTimes + advertisementPositionCategory2DisplayTimes + advertisementPositionCategory3DisplayTimes + advertisementPositionCategory4DisplayTimes;
    }

    /**
     * 当前日期的 Android 展示次数
     */
    public Long getAndroidDisplayTimes() {
        return advertisementPositionCategory5DisplayTimes + advertisementPositionCategory6DisplayTimes;
    }

    /**
     * 当前日期的 IOS 展示次数
     */
    public Long getIOSDisplayTimes() {
        return advertisementPositionCategory7DisplayTimes + advertisementPositionCategory8DisplayTimes;
    }
}
