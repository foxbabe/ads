package com.sztouyun.advertisingsystem.model.monitor;

import lombok.Data;

@Data
public class ClickOrScanTimes {
    /**
     * 广告位类别 {@link com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum}
     */
    private Integer advertisementPositionCategory;

    /**
     * 素材链接类型 {@link com.sztouyun.advertisingsystem.model.system.MaterialLinkTypeEnum}
     */
    private Integer linkType;

    /**
     * 点击或扫码次数
     */
    private Long totalTimes;
}
