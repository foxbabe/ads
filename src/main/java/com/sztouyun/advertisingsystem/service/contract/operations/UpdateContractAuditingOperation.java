package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateContractAuditingOperation implements IActionOperation<ContractOperationLog> {
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void operateAction(ContractOperationLog contractOperationLog) {
        Contract contract =contractOperationLog.getContract();
        if(contract == null){
            contract = contractRepository.findOne(contractOperationLog.getContractId());
        }
        ContractOperationEnum operationEnum = contractOperationLog.getContractOperationEnum();
        if(operationEnum.equals(ContractOperationEnum.SubmitTermination)){
            contract.setAuditing(true);
            contract.setAuditingRemark(contractOperationLog.getRemark());
        }else if(contractOperationLog.isFinishAuditing())
        {
            contract.setAuditing(false);
            contract.setAuditingRemark("");
        }
        contractRepository.save(contract);
    }
}
