package com.sztouyun.advertisingsystem.viewmodel.message.customer;

import com.sztouyun.advertisingsystem.model.message.MessageTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;

public class CustomerMessageViewModel extends MessageViewModel {
    public CustomerMessageViewModel(){
        setMessageType(MessageTypeEnum.Customer.getValue());
    }

    private String customerId;

    private String customerName;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CustomerMessageViewModel setNames(String creatorName,String customerName,String oldOwnerName,String newOwnerName){
        this.customerName=customerName;
        return this;
    }

    public void setOwnerIds(String oldOwnerId,String newOwnerId){

    }

}
