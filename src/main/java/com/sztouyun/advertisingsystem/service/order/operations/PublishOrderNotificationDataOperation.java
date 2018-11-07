package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.PublishOrderNotificationData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublishOrderNotificationDataOperation extends BaseService  implements IActionOperation<OrderOperationLog> {
    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        PublishOrderNotificationData publishOrderNotificationData = new PublishOrderNotificationData();
        publishOrderNotificationData.setPartnerId(orderOperationLog.getOrderInfo().getPartnerId());
        publishOrderNotificationData.setThirdPartId(orderOperationLog.getOrderInfo().getThirdPartId());
        publishOrderNotificationData.setSuccess(orderOperationLog.isSuccessed());
        publishOrderNotificationData.setMessage(orderOperationLog.getRemark());

        publishOpenApiNotification(publishOrderNotificationData);
    }
}
