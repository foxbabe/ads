package com.sztouyun.advertisingsystem.service.profitshare.operations.Info;

import com.sztouyun.advertisingsystem.service.rule.base.data.RuleInfo;

import java.util.Date;

public class ProfitShareCalculationRuleInfo extends RuleInfo<Double> {
    private String advertisementId;
    private String storeId;
    private Date dateTime;

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
