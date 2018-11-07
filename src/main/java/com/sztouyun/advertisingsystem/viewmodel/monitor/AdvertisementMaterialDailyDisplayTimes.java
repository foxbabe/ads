package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.Data;

@Data
public class AdvertisementMaterialDailyDisplayTimes {
    private Long datetime;
    private Integer times;
    private String advertisementMaterialId;
    private Integer advertisementPositionType;
    private Integer terminalType;
    private String storeId="";
}
