package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderOperationEnum;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidateOrderStatusOperation implements IActionOperation<OrderOperationLog> {
    private Map<OrderStatusEnum,List<OrderOperationEnum>> orderStatusMapping = new HashMap<OrderStatusEnum,List<OrderOperationEnum>>() {
        {
            put(OrderStatusEnum.PendingPublishing, Arrays.asList(OrderOperationEnum.Cancel,OrderOperationEnum.SubmitDeliveryAuditing));
            put(OrderStatusEnum.PendingDelivery, Arrays.asList(OrderOperationEnum.Cancel,OrderOperationEnum.AutoDelivery));
            put(OrderStatusEnum.PublishingAuditing, Arrays.asList(OrderOperationEnum.Cancel,OrderOperationEnum.DeliveryAuditing));
            put(OrderStatusEnum.Delivering, Arrays.asList(OrderOperationEnum.TakeOff,OrderOperationEnum.Finish));
        }
    };

    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        OrderInfo orderInfo = orderOperationLog.getOrderInfo();
        List<OrderOperationEnum> validOperationEnums = orderStatusMapping.get(orderInfo.getOrderStatusEnum());
        if (validOperationEnums ==null || !validOperationEnums.contains(orderOperationLog.getOrderOperationEnum()))
            throw new BusinessException(orderInfo.getOrderStatusEnum().getDisplayName()+"的订单不支持操作");
    }
}
