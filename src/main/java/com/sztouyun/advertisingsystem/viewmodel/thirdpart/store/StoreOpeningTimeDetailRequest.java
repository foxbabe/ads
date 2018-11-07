package com.sztouyun.advertisingsystem.viewmodel.thirdpart.store;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;

import java.util.Date;

public class StoreOpeningTimeDetailRequest extends BasePageInfo {

    public StoreOpeningTimeDetailRequest(Date date, String shopId) {
        this.date = date;
        this.shopId = shopId;
    }

    public StoreOpeningTimeDetailRequest() {
    }

    private Date date;
    private String shopId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
