package com.sztouyun.advertisingsystem.viewmodel.thirdpart.store;

import java.util.Date;
import java.util.List;

public class StoreOpeningTimeRequest {

    public StoreOpeningTimeRequest() {
    }

    public StoreOpeningTimeRequest(Date date, List<String> storeIds) {
        this.date = date;
        this.shopIds = storeIds;
    }

    private Date date;
    private List<String> shopIds;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<String> shopIds) {
        this.shopIds = shopIds;
    }
}
