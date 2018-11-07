package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.sztouyun.advertisingsystem.viewmodel.common.BaseAuthenticationInfo;

public class CustomerCityAreaStatistic extends BaseAuthenticationInfo {

    private String provinceId;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
}
