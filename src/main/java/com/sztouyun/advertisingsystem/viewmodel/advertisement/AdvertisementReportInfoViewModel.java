package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/9/11.
 */
@ApiModel
public class AdvertisementReportInfoViewModel {
    @ApiModelProperty(value = "城市名称)")
    private String cityName;
    @ApiModelProperty(value = "省名称)")
    private String provinceName;
    @ApiModelProperty(value = "投放总数)")
    private Long storeCount;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Long storeCount) {
        this.storeCount = storeCount;
    }
}
