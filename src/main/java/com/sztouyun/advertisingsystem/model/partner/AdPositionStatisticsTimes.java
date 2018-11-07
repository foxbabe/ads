package com.sztouyun.advertisingsystem.model.partner;

import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

@Data
public class AdPositionStatisticsTimes {
    private Integer advertisementPositionCategory;

    private Long validTimes;

    private Long invalidTimes;

    private Long displayTimes;

    public String getAdvertisementPositionCategoryName() {
        return EnumUtils.getDisplayName(advertisementPositionCategory, AdvertisementPositionCategoryEnum.class);
    }

    public Long getInvalidTimes() {
        return displayTimes-validTimes;
    }
}
