package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecreaseContractStoreUsedCountOperation  implements IActionOperation<Contract> {
    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Override
    public void operateAction(Contract contract) {
        if(contract.getContractExtension().getHasCashRegister()) {
            storeInfoMapper.decreaseContractStoreUsedCount(contract.getId());
        }
    }
}
