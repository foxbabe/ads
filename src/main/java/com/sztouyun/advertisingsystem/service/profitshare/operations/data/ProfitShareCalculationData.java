package com.sztouyun.advertisingsystem.service.profitshare.operations.data;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.ProfitShareCalculationRuleInfo;

import java.util.Date;


public class ProfitShareCalculationData extends ProfitShareRuleData<ProfitShareCalculationRuleInfo> {
    private StoreInfo store;
    private Advertisement advertisement;

    public ProfitShareCalculationData(String objectId, Date dateTime, StoreInfo store, Advertisement advertisement) {
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
