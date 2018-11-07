package com.sztouyun.advertisingsystem.service.task.advertisement.data;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import java.util.Date;

@Data
public class AdvertisementStorePeriodInfo extends AdvertisementStoreInfo {
    private Date startTime;
    private Boolean activated;

    public AdvertisementStorePeriodInfo(Advertisement advertisement, StoreInfo storeInfo, Date startTime, Boolean activated) {
        super(advertisement, storeInfo);
        this.startTime = startTime;
        this.activated = activated;
    }

    public AdvertisementStorePeriodInfo(AdvertisementStoreInfo data, Date startTime, Boolean activated) {
        super(data.getAdvertisement(), data.getStoreInfo(), data.getDate());
        this.startTime = startTime;
        this.activated = activated;
    }
}
