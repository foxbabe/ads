package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum ContractOperationEnum  implements EnumMessage<Integer> {
    Submit(1,"提交合同审核"),
    Auditing(2,"合同审核"),
    Sign(3,"签约"),
    Finish(4,"完成"),
    SubmitTermination(5,"提交终止审核"),
    TerminationAuditing(6,"终止审核"),
    BeginExecute(11,"开始执行"),
    StageFinish(12,"阶段完成"),
    Edit(13,"编辑");//管理员编辑待审核合同

    private Integer value;
    private String displayName;

    ContractOperationEnum(Integer value,String displayName) {
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
