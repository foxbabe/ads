package com.sztouyun.advertisingsystem.model.contract;

/**
 * Created by wenfeng on 2017/8/7.
 */
public class StoreForm {
    private String storeName;
    private String storeAddress;
    private Double storeCost;
    private String remark;

    public String getStoreName() {
        return storeName;
    }
    public StoreForm(){}
    public StoreForm(String storeName,String storeAddress,Double storeCost,String remark){
        this.storeName=storeName;
        this.storeAddress=storeAddress;
        this.storeCost=storeCost;
        this.remark=remark;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Double getStoreCost() {
        return storeCost;
    }

    public void setStoreCost(Double storeCost) {
        this.storeCost = storeCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
