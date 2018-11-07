package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum ContractStatusEnum  implements EnumMessage<Integer> {

    PendingCommit(1,"待提交"),
    PendingAuditing(2,"待审核"),
    PendingSign(3,"待签约"),
    PendingExecution(4,"待执行"),
    AbruptlyTerminated(5,"意外终止"),
    Finished(6,"执行完成"),
    Executing(7,"执行中");

    private Integer value;
    private String displayName;

    ContractStatusEnum(Integer value,String displayName) {
        this.value = value;
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
}
