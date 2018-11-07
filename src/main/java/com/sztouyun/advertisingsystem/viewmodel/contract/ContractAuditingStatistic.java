package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractAuditingStatistic {
    @ApiModelProperty(value = "待合同审核数量")
    private int  pendingAuditingCount;
    @ApiModelProperty(value = "待终止审核数量")
    private int  pendingTerminationAuditingCount;

    public int getPendingAuditingCount() {
        return pendingAuditingCount;
    }

    public void setPendingAuditingCount(int pendingAuditingCount) {
        this.pendingAuditingCount = pendingAuditingCount;
    }

    public int getPendingTerminationAuditingCount() {
        return pendingTerminationAuditingCount;
    }

    public void setPendingTerminationAuditingCount(int pendingTerminationAuditingCount) {
        this.pendingTerminationAuditingCount = pendingTerminationAuditingCount;
    }
}
