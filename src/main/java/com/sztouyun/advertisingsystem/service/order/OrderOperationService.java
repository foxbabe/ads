package com.sztouyun.advertisingsystem.service.order;

import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.service.order.operations.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderOperationService extends BaseOperationService<OrderOperationLog,Void> {
    @Autowired
    private OrderService orderService;

    @Override
    protected void onOperating(OrderOperationLog orderOperationLog, IOperationCollection<OrderOperationLog, Void> operationCollection) {
        OrderInfo orderInfo;
        if(!StringUtils.isEmpty(orderOperationLog.getOrderId())){
            orderInfo =orderService.getOrder(orderOperationLog.getOrderId());
        }else {
            orderInfo = orderService.getOrder(orderOperationLog.getPartnerId(),orderOperationLog.getThirdPartId());
        }
        if(orderInfo ==null)
            throw new BusinessException("订单不存在");

        orderOperationLog.setOrderInfo(orderInfo);
        orderOperationLog.setOrderId(orderInfo.getId());
        orderOperationLog.setPartnerId(orderInfo.getPartnerId());
        orderOperationLog.setThirdPartId(orderInfo.getThirdPartId());
        operationCollection.add(ValidateCoorperationPartnerOperation.class);
        operationCollection.add(ValidateOrderStatusOperation.class);
        switch (orderOperationLog.getOrderOperationEnum()){
            case SubmitDeliveryAuditing:
                operationCollection.add(ValidateOrderMaterialStatusOperation.class);
                break;
        }
        operationCollection.add(UpdateOrderStatusOperation.class);
        operationCollection.add(SaveOrderOperationLogOperation.class);
    }

    @Override
    protected void onOperated(OrderOperationLog orderOperationLog, IOperationCollection<OrderOperationLog, Void> operationCollection) {
        OrderInfo orderInfo = orderOperationLog.getOrderInfo();
        switch (orderOperationLog.getOrderOperationEnum()){
            case AutoDelivery:
                operationCollection.add(UpdateOrderEffectiveStartTimeOperation.class,orderInfo);
                operationCollection.add(CreateOrderMonitorOperation.class,orderInfo);
                operationCollection.add(CreateOrderDailyMonitorOperation.class,orderInfo);
                break;
            case SubmitDeliveryAuditing:
                operationCollection.add(UpdateOrderMaterialOperation.class);
                break;
            case DeliveryAuditing:
                operationCollection.add(PublishOrderNotificationDataOperation.class);
                LocalDate date = new LocalDate(orderInfo.getStartTime());
                if(date.compareTo(LocalDate.now()) <=0) {
                    operationCollection.add(CreateOrderMonitorOperation.class, orderInfo);
                }
                break;
            case Finish:
                operationCollection.add(UpdateOrderEffectiveEndTimeOperation.class,orderInfo);
                break;
            case TakeOff:
                operationCollection.add(UpdateOrderEffectiveEndTimeOperation.class,orderInfo);
                break;
        }
    }
}
