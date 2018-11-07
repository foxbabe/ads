package com.sztouyun.advertisingsystem.viewmodel.monitor;

/**
 * Created by wenfeng on 2017/11/1.
 */
public class ContractDeliveryStatistic {
    private String contractId;
    private Long deliveryTimes;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Long getDeliveryTimes() {
        return deliveryTimes;
    }

    public void setDeliveryTimes(Long deliveryTimes) {
        this.deliveryTimes = deliveryTimes;
    }
}