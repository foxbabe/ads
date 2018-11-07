package com.sztouyun.advertisingsystem.service.event;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.model.customer.CustomerOperationLog;

public class CustomerOperationEvent extends BaseEvent<CustomerOperationLog> {

    public CustomerOperationEvent(CustomerOperationLog customerOperationLog) {
        super(customerOperationLog);
    }
}
