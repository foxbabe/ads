package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateContractStatusOperation implements IActionOperation<ContractOperationLog> {
    @Autowired
    private ContractRepository contractRepository;

    private Map<ContractOperationEnum, ContractStatusEnum[]> contractOperationEnumMapping = new HashMap<ContractOperationEnum, ContractStatusEnum[]>() {
        {
            put(ContractOperationEnum.Submit, new ContractStatusEnum[]{ContractStatusEnum.PendingAuditing, ContractStatusEnum.PendingCommit});
            put(ContractOperationEnum.Auditing, new ContractStatusEnum[]{ContractStatusEnum.PendingSign, ContractStatusEnum.PendingCommit});
            put(ContractOperationEnum.Sign, new ContractStatusEnum[]{ContractStatusEnum.PendingExecution, ContractStatusEnum.PendingCommit});
            put(ContractOperationEnum.BeginExecute, new ContractStatusEnum[]{ContractStatusEnum.Executing, ContractStatusEnum.Executing});
            put(ContractOperationEnum.StageFinish, new ContractStatusEnum[]{ContractStatusEnum.PendingExecution, ContractStatusEnum.PendingExecution});
            put(ContractOperationEnum.Finish, new ContractStatusEnum[]{ContractStatusEnum.Finished, ContractStatusEnum.AbruptlyTerminated});
        }
    };

    @Override
    @Transactional
    public void operateAction(ContractOperationLog contractOperationLog) {
        Contract contract =contractOperationLog.getContract();
        if(contract == null){
            contract = contractRepository.findOne(contractOperationLog.getContractId());
        }
        ContractOperationEnum operationEnum = contractOperationLog.getContractOperationEnum();
        if(!contractOperationEnumMapping.containsKey(operationEnum))
            return;
        //更新合同状态
        ContractStatusEnum contractStatus = contractOperationEnumMapping.get(operationEnum)[contractOperationLog.isSuccessed() ? 0 : 1];
        contract.setContractStatus(contractStatus.getValue());
        contractRepository.saveAndFlush(contract);
    }
}
