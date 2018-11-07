package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class AdvertisementPositionAreaMapViewModel {

    @ApiModelProperty(value = "区域最小广告位数量")
    private Integer minAmount = 0;

    @ApiModelProperty(value = "区域最大广告位数量")
    private Integer maxAmount = 0;

    @ApiModelProperty(value = "区域广告位列表")
    private List<AdvertisementPositionAreaStatisticViewModel> list;

    @ApiModelProperty(value = "广告位总数")
    private Integer advertisementPositionCount = 0;

    @ApiModelProperty(value = "已经占用广告位总数")
    private Integer useAdvertisementPositionCount = 0;

    @ApiModelProperty(value = "可用广告位总数")
    private Integer availableAdvertisementPositionCount = 0;

    @ApiModelProperty(value = "门店数")
    private  List<DistributionStatisticDto> storeAmountList;

    public Integer getAdvertisementPositionCount() {
        return advertisementPositionCount;
    }

    public void setAdvertisementPositionCount(Integer advertisementPositionCount) {
        this.advertisementPositionCount = advertisementPositionCount;
    }

    public Integer getUseAdvertisementPositionCount() {
        return useAdvertisementPositionCount;
    }

    public void setUseAdvertisementPositionCount(Integer useAdvertisementPositionCount) {
        this.useAdvertisementPositionCount = useAdvertisementPositionCount;
    }

    public Integer getAvailableAdvertisementPositionCount() {
        return availableAdvertisementPositionCount;
    }

    public void setAvailableAdvertisementPositionCount(Integer availableAdvertisementPositionCount) {
        this.availableAdvertisementPositionCount = availableAdvertisementPositionCount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<AdvertisementPositionAreaStatisticViewModel> getList() {
        return list;
    }

    public void setList(List<AdvertisementPositionAreaStatisticViewModel> list) {
        this.list = list;
    }

    public List<DistributionStatisticDto> getStoreAmountList() {
        return storeAmountList;
    }

    public void setStoreAmountList(List<DistributionStatisticDto> storeAmountList) {
        this.storeAmountList = storeAmountList;
    }
}
