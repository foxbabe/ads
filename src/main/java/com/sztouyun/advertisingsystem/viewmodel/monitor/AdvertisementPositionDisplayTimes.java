package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;
import lombok.experimental.var;

@Data
public class AdvertisementPositionDisplayTimes {
    /**
     * 广告位置
     */
    private Integer advertisementPositionCategory;

    /**
     * 展示次数
     */
    private Integer displayTimes;

    public Integer getTerminalType(){
        var  positionCategoryEnum= EnumUtils.toEnum(getAdvertisementPositionCategory(),AdvertisementPositionCategoryEnum.class);
        if(positionCategoryEnum ==null)
            return 0;
        return positionCategoryEnum.getTerminalType().getValue();
    }
}
