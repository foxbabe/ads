package com.sztouyun.advertisingsystem.viewmodel.customer;

public class CustomerAdvertisementStatistic {
    private String customerId;
    private Long advertisementDeliveryTimes;
    private Integer advertisingDeliveringCount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getAdvertisementDeliveryTimes() {
        if(advertisementDeliveryTimes ==null)
            return new Long(0);
        return advertisementDeliveryTimes;
    }

    public void setAdvertisementDeliveryTimes(Long advertisementDeliveryTimes) {
        this.advertisementDeliveryTimes = advertisementDeliveryTimes;
    }

    public Integer getAdvertisingDeliveringCount() {
        return advertisingDeliveringCount;
    }

    public void setAdvertisingDeliveringCount(Integer advertisingDeliveringCount) {
        this.advertisingDeliveringCount = advertisingDeliveringCount;
    }
}
