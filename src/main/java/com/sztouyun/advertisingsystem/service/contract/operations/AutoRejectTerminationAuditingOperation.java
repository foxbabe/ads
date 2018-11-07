package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.service.contract.ContractOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//自动驳回终止审核
@Service
public class AutoRejectTerminationAuditingOperation implements IActionOperation<Contract> {

    @Autowired
    private ContractOperationService contractOperationService;

    @Override
    public void operateAction(Contract contract) {
        contractOperationService.operate(new ContractOperationLog(contract.getId(), ContractOperationEnum.TerminationAuditing.getValue(),false,"")
                ,SaveContractOperationLogOperation.class);
    }
}
