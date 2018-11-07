package com.sztouyun.advertisingsystem.service.message.event;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;

public class ContractOperationEvent extends BaseEvent<ContractOperationLog> {

    public ContractOperationEvent(ContractOperationLog contractOperationLog) {
        super(contractOperationLog);
    }
}
