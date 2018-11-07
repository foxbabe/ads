package com.sztouyun.advertisingsystem.viewmodel.message.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AdvertisementInfo {

    @ApiModelProperty(value = "广告Id")
    private String advertisementId;
    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    public AdvertisementInfo() {
    }

    public AdvertisementInfo(String advertisementId, String advertisementName) {
        this.advertisementId = advertisementId;
        this.advertisementName = advertisementName;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }
}
