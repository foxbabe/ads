package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdvertisementActiveUpdateRequest {
    private String advertisementId;

    private List<String> storeIds;

    private Date profitDate;

    public AdvertisementActiveUpdateRequest(String advertisementId, List<String> storeIds, Date profitDate) {
        this.advertisementId = advertisementId;
        this.storeIds = storeIds;
        this.profitDate = profitDate;
    }
    public AdvertisementActiveUpdateRequest(){}
}
