package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import java.util.Date;
import java.util.List;

public class SupplementaryProfitShareRequest {
    private List<String> storeIds;

    private Date profitDate;

    public SupplementaryProfitShareRequest() {
    }

    public SupplementaryProfitShareRequest(List<String> storeIds, Date profitDate) {
        this.storeIds = storeIds;
        this.profitDate = profitDate;
    }

    public List<String> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<String> storeIds) {
        this.storeIds = storeIds;
    }

    public Date getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(Date profitDate) {
        this.profitDate = profitDate;
    }
}
