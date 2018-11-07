package com.sztouyun.advertisingsystem.model.monitor;

import lombok.Data;

@Data
public class StoreParticipationStatistics {
    /**
     * 广告位置类别 {@link com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum}
     */
    private Integer advertisementPositionCategory;

    /**
     * 素材链接类型 {@link com.sztouyun.advertisingsystem.model.system.MaterialLinkTypeEnum}
     */
    private Integer linkType;

    /**
     * 参与门店数量
     */
    private Long count;
}
