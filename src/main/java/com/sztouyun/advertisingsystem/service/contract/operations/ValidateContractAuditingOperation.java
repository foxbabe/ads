package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateContractAuditingOperation implements IActionOperation<ContractOperationLog> {
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void operateAction(ContractOperationLog contractOperationLog) {
        Contract contract =contractOperationLog.getContract();
        if(contract == null){
            contract = contractRepository.findOne(contractOperationLog.getContractId());
        }
        if (contractOperationLog.isFinishAuditing() && !contract.isAuditing())
            throw new BusinessException("当前合同状态不支持本操作！");
    }
}
