package com.sztouyun.advertisingsystem.viewmodel.partner.store;

import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import lombok.Data;

@Data
public class StorePositionCountInfo {

    private int advertisementPositionType = AdvertisementPositionTypeEnum.FullScreen.getValue();

    private long positionCount;
}
