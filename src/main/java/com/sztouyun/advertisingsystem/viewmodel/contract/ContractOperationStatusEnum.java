package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;

/**
 * Created by szty on 2017/7/28.
 */
public enum ContractOperationStatusEnum implements EnumMessage<Integer> {
    All(0,null,null,"全部状态"),
    Submit(1,ContractOperationEnum.Submit,null,"提交审核"),
    SubmitSucceed(2,ContractOperationEnum.Auditing,true,"审核通过"),
    SubmitFailed(3,ContractOperationEnum.Auditing,false,"审核驳回"),
    SignSucceed(4,ContractOperationEnum.Sign,true,"签约成功"),
    SignFailed(5,ContractOperationEnum.Sign,false,"签约失败"),
    Executing(6,ContractOperationEnum.BeginExecute,null,"执行中"),
    PendingExecution(7,ContractOperationEnum.StageFinish,null,"待执行"),
    SubmitTermination(8,ContractOperationEnum.SubmitTermination,true,"提交终止审核"),
    AbortContract(9,ContractOperationEnum.Finish,false,"终止合同"),
    TerminationAuditing(10,ContractOperationEnum.TerminationAuditing,false,"终止驳回"),
    Finish(11,ContractOperationEnum.Finish,true,"合同完成"),
    PendingCommit(12,ContractOperationEnum.Edit,null,"待提交");


    private Integer value;
    private Integer operation;
    private Boolean succeeded;
    private String displayName;

    ContractOperationStatusEnum(Integer value,ContractOperationEnum operation, Boolean succeeded,String displayName) {
        this.value = value;
        this.operation=operation==null?null:operation.getValue();
        this.succeeded=succeeded;
        this.displayName = displayName;
    }
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Integer getOperation() {
        return operation;
    }


    public Boolean getSucceeded() {
        return succeeded;
    }
}
