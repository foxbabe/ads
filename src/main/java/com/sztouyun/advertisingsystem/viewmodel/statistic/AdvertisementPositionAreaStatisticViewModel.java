package com.sztouyun.advertisingsystem.viewmodel.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AdvertisementPositionAreaStatisticViewModel {
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "广告位总数量")
    private Integer advertisementPositionCount;

    @ApiModelProperty(value = "广告位占比")
    private String advertisementPositionRatio ;

    @ApiModelProperty(value = "未使用广告位数量")
    private Integer availableAdvertisementPositionCount;

    @ApiModelProperty(value = "已使用广告位数量")
    private Integer usedAdvertisementPositionCount;

    @ApiModelProperty(value = "广告位使用率")
    private String useAdvertisementPositionRatio;

    @ApiModelProperty(value = "当前区域门店数量")
    private Integer storeAmount;

    @ApiModelProperty(value = "当前区域门店占比")
    private String storeRatio;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAdvertisementPositionCount() {
        return advertisementPositionCount;
    }

    public void setAdvertisementPositionCount(Integer advertisementPositionCount) {
        this.advertisementPositionCount = advertisementPositionCount;
    }

    public String getAdvertisementPositionRatio() {
        return advertisementPositionRatio;
    }

    public void setAdvertisementPositionRatio(String advertisementPositionRatio) {
        this.advertisementPositionRatio = advertisementPositionRatio;
    }

    public String getUseAdvertisementPositionRatio() {
        return useAdvertisementPositionRatio;
    }

    public void setUseAdvertisementPositionRatio(String useAdvertisementPositionRatio) {
        this.useAdvertisementPositionRatio = useAdvertisementPositionRatio;
    }

    public Integer getAvailableAdvertisementPositionCount() {
        return availableAdvertisementPositionCount;
    }

    public void setAvailableAdvertisementPositionCount(Integer availableAdvertisementPositionCount) {
        this.availableAdvertisementPositionCount = availableAdvertisementPositionCount;
    }

    public Integer getUsedAdvertisementPositionCount() {
        return usedAdvertisementPositionCount;
    }

    public void setUsedAdvertisementPositionCount(Integer usedAdvertisementPositionCount) {
        this.usedAdvertisementPositionCount = usedAdvertisementPositionCount;
    }

    public Integer getStoreAmount() {
        return storeAmount;
    }

    public void setStoreAmount(Integer storeAmount) {
        this.storeAmount = storeAmount;
    }

    public String getStoreRatio() {
        return storeRatio;
    }

    public void setStoreRatio(String storeRatio) {
        this.storeRatio = storeRatio;
    }
}
