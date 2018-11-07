package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

/**
 * Created by wenfeng on 2018/1/18.
 */
public class StoreProfitBean extends BaseStoreProfit {
    private  String provinceId;

    private  String cityId;

    private  String regionId;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
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
