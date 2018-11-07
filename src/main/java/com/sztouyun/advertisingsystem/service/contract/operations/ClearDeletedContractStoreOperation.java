package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.mapper.ContractMapper;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClearDeletedContractStoreOperation implements IActionOperation<Contract> {
    @Autowired
    private ContractMapper contractMapper;

    @Override
    public void operateAction(Contract contract) {
        if(contract.getContractExtension().getHasCashRegister()) {
            contractMapper.clearDeletedContractStore(contract.getId());
        }
    }
}
