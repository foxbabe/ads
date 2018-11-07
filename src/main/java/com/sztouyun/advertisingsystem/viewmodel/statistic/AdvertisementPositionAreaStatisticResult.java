package com.sztouyun.advertisingsystem.viewmodel.statistic;

public class AdvertisementPositionAreaStatisticResult {
    private String areaName;

    private Integer advertisementPositionCount;

    private Long storeAmount;

    private Integer availableAdvertisementPositionCount;

    private Integer usedAdvertisementPositionCount;

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

    public Long getStoreAmount() {
        return storeAmount;
    }

    public void setStoreAmount(Long storeAmount) {
        this.storeAmount = storeAmount;
    }
}
