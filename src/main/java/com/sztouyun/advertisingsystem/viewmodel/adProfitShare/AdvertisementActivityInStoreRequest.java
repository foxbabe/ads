package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import java.util.Date;

public class AdvertisementActivityInStoreRequest {
    private String storeId;
    private Date effectiveStartDate;
    private Date effectiveEndDate;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(Date effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(Date effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public AdvertisementActivityInStoreRequest() {
    }

    public AdvertisementActivityInStoreRequest(String storeId, Date effectiveStartDate, Date effectiveEndDate) {
        this.storeId = storeId;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
    }
}
