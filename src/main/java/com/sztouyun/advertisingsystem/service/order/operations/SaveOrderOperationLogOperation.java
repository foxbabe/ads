package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.repository.order.OrderOperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveOrderOperationLogOperation implements IActionOperation<OrderOperationLog> {
    @Autowired
    private OrderOperationLogRepository orderOperationLogRepository;

    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        orderOperationLogRepository.save(orderOperationLog);
    }
}
