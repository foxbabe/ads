package com.sztouyun.advertisingsystem.service.advertisement.operations.data;

import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;

public class AdvertisementContractActionInfo {
    private ContractOperationLog contractOperationLog;
    private String advertisementId;

    public AdvertisementContractActionInfo(ContractOperationLog contractOperationLog, String advertisementId) {
        this.contractOperationLog = contractOperationLog;
        this.advertisementId = advertisementId;
    }

    public ContractOperationLog getContractOperationLog() {
        return contractOperationLog;
    }

    public void setContractOperationLog(ContractOperationLog contractOperationLog) {
        this.contractOperationLog = contractOperationLog;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }
}
