package com.sztouyun.advertisingsystem.service.profitshare.operations.data;

import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.StoreValidationRuleInfo;

import java.util.Date;


public class StoreValidationRuleData extends ProfitShareRuleData<StoreValidationRuleInfo> {
    private StoreInfo store;

    public StoreValidationRuleData(String objectId, StoreInfo store, Date dateTime) {
        super(objectId,dateTime);
        this.store = store;
    }

    public StoreInfo getStore() {
        return store;
    }

    public void setStore(StoreInfo store) {
        this.store = store;
    }
}
