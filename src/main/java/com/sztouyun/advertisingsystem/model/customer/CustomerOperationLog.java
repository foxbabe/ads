package com.sztouyun.advertisingsystem.model.customer;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.*;

@Entity
public class CustomerOperationLog extends BaseModel {
    public CustomerOperationLog(){

    }

    public CustomerOperationLog(Integer customerOperationTypeEnum, String customerId, String oldOwnerId,String newOwnerId) {
        this.operation = customerOperationTypeEnum;
        this.customerId = customerId;
        this.oldOwnerId = oldOwnerId;
        this.newOwnerId=newOwnerId;
    }

    @Column(nullable = false)
    private Integer operation;

    @Column(name = "customer_id",nullable = false, length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    @Column(length = 36)
    private String oldOwnerId ="";

    @Column(length = 36)
    private String newOwnerId ="";

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(String oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public String getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(String newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
