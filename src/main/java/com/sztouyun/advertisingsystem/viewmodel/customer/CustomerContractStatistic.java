package com.sztouyun.advertisingsystem.viewmodel.customer;

public class CustomerContractStatistic {
    private String customerId;
    private double contractTotalAmount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getContractTotalAmount() {
        return contractTotalAmount;
    }

    public void setContractTotalAmount(double contractTotalAmount) {
        this.contractTotalAmount = contractTotalAmount;
    }
}
