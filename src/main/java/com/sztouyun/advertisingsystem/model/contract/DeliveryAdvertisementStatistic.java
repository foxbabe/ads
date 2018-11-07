package com.sztouyun.advertisingsystem.model.contract;

/**
 * Created by wenfeng on 2018/1/19.
 */
public class DeliveryAdvertisementStatistic {
    private String contractId;
    private Long deliveryCount;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Long getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(Long deliveryCount) {
        this.deliveryCount = deliveryCount;
    }
}
