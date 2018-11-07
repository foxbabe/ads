package com.sztouyun.advertisingsystem.service.contract;

import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.service.common.operations.ValidateAuditingPermissionOperation;
import com.sztouyun.advertisingsystem.service.contract.operations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractOperationService extends BaseOperationService<ContractOperationLog,Void> {
    @Autowired
    private ContractService contractService;

    @Override
    protected void onOperating(ContractOperationLog contractOperationLog, IOperationCollection<ContractOperationLog,Void> operationCollection) {
        if(contractOperationLog.getContractOperationEnum() == null)
            throw new BusinessException("操作类型错误！");

        Contract contract = contractService.getContract(contractOperationLog.getContractId());
        contractOperationLog.setContract(contract);
        operationCollection.add(ValidateContractStatusOperation.class);
        operationCollection.add(ValidateContractAuditingOperation.class);
        switch (contractOperationLog.getContractOperationEnum()){
            case Submit:
                operationCollection.add(ValidateContractStoreCountOperation.class,contractOperationLog.getContract());
                operationCollection.add(ValidateContractAdvertisementPositionConfigEnable.class,contractOperationLog.getContract());
                break;
            case Auditing:
            case TerminationAuditing:
                operationCollection.add(ValidateAuditingPermissionOperation.class,null);
                break;
            case Finish:
                if(!contractOperationLog.isSuccessed()){
                    //合同终止验证用户权限
                    operationCollection.add(ValidateAuditingPermissionOperation.class,null);
                }else {
                    if(contract.isAuditing()){
                        //合同下架或者执行完成时先自动驳回终止审批
                        operationCollection.add(AutoRejectTerminationAuditingOperation.class,contract);
                    }
                }
                break;
        }
        operationCollection.add(SaveContractOperationLogOperation.class);
        operationCollection.add(UpdateContractStatusOperation.class);
        operationCollection.add(UpdateContractAuditingOperation.class);
    }

    @Override
    protected void onOperated(ContractOperationLog contractOperationLog, IOperationCollection<ContractOperationLog,Void> operationCollection) {
        Contract contract = contractOperationLog.getContract();
        switch (contractOperationLog.getContractOperationEnum()){
            case Submit:
                //合同提交审核时清除选中的并且为已删除的门店
                operationCollection.add(ClearDeletedContractStoreOperation.class,contractOperationLog.getContract());
                break;
            case Sign:
                if(contractOperationLog.isSuccessed()){
                    operationCollection.add(UpdateSignerAndSignTimeOperation.class,contractOperationLog.getContract());
                }
                break;
            case BeginExecute:
                operationCollection.add(UpdateStoreIsUsingOperation.class,contract);
                break;
            case StageFinish:
                operationCollection.add(UpdateUsedContractPeriodOperation.class,contract);
                operationCollection.add(UpdateStoreIsUsingOperation.class,contract);
                break;
            case Finish:
                operationCollection.add(DecreaseContractStoreUsedCountOperation.class,contract);
                //下架合同广告
                operationCollection.add(TakeOffContractAdvertisementOperation.class,contract);
                operationCollection.add(UpdateUsedContractPeriodOperation.class,contract);
                operationCollection.add(UpdateStoreIsUsingOperation.class,contract);
                break;
        }
    }
}
