package com.sztouyun.advertisingsystem.service.profitshare.operations.data;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.AdvertisementValidationRuleInfo;

import java.util.Date;


public class AdvertisementValidationRuleData extends ProfitShareRuleData<AdvertisementValidationRuleInfo> {
    private StoreInfo store;
    private Advertisement advertisement;

    public AdvertisementValidationRuleData(String objectId, Date dateTime, StoreInfo store, Advertisement advertisement) {
        super(objectId, dateTime);
        this.store = store;
        this.advertisement = advertisement;
    }

    public StoreInfo getStore() {
        return store;
    }

    public void setStore(StoreInfo store) {
        this.store = store;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
