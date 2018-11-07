package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.service.advertisement.operations.data.AdvertisementContractActionInfo;
import com.sztouyun.advertisingsystem.service.contract.ContractOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementContractActionOperation implements IActionOperation<AdvertisementContractActionInfo> {
    @Autowired
    private ContractOperationService operationService;

    @Override
    public void operateAction(AdvertisementContractActionInfo advertisementContractActionInfo) {
        ContractOperationLog contractOperationLog = advertisementContractActionInfo.getContractOperationLog();
        contractOperationLog.setAdvertisementId(advertisementContractActionInfo.getAdvertisementId());
        operationService.operate(contractOperationLog);
    }
}
