package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.utils.UUIDUtils;


public class ContractStoreInfoViewModel {
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 设备编码
     */
    private String deviceId;
    /**
     * 门店地址
     */
    private String storeAddress;
    /**
     * 城市id
     */
    private String cityId;
    /**
     * 区域id
     */
    private String regionId;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
