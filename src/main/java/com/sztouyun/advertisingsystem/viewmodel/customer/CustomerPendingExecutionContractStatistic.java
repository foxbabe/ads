package com.sztouyun.advertisingsystem.viewmodel.customer;

public class CustomerPendingExecutionContractStatistic {
    private String customerId;
    private Long pendingExecutionContractCount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getPendingExecutionContractCount() {
        return pendingExecutionContractCount ==null ? 0 : pendingExecutionContractCount;
    }

    public void setPendingExecutionContractCount(Long pendingExecutionContractCount) {
        this.pendingExecutionContractCount = pendingExecutionContractCount;
    }
}
