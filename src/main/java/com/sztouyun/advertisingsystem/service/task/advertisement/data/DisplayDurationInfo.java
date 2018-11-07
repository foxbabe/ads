package com.sztouyun.advertisingsystem.service.task.advertisement.data;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import java.util.Date;

@Data
public class DisplayDurationInfo extends AdvertisementStoreInfo {
    private Date startDate;

    private Date endDate;

    private Integer notDisplayDurationConfig;

    public DisplayDurationInfo(Advertisement advertisement, StoreInfo storeInfo, Date startDate, Date endDate, Integer notDisplayDurationConfig) {
        super(advertisement, storeInfo);
        this.startDate = startDate;
        this.endDate = endDate;
        this.notDisplayDurationConfig = notDisplayDurationConfig;
    }
}
