package com.sztouyun.advertisingsystem.model.monitor;

import lombok.Data;

@Data
public class AdvertisementPositionCategoryDisplayTimes {
    /**
     * 广告位置, AdvertisementPositionCategoryEnum
     */
    private Integer advertisementPositionCategory;

    /**
     * 展示次数
     */
    private Integer displayTimes;
}
