package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.mapper.OrderDailyStoreMonitorStaticMapper;
import com.sztouyun.advertisingsystem.viewmodel.monitor.OrderDateDto;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderOperationEnum;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderOperationLogRepository;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateOrderStatusOperation implements IActionOperation<OrderOperationLog> {
    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private OrderOperationLogRepository orderOperationLogRepository;
    @Autowired
    private OrderDailyStoreMonitorStaticMapper orderDailyStoreMonitorStaticMapper;


    private Map<OrderOperationEnum,OrderStatusEnum[]> orderOperationEnumMapping = new HashMap<OrderOperationEnum,OrderStatusEnum[]>() {
        {
            put(OrderOperationEnum.SubmitDeliveryAuditing, new OrderStatusEnum[]{OrderStatusEnum.PublishingAuditing, OrderStatusEnum.PublishingAuditing});
            put(OrderOperationEnum.DeliveryAuditing, new OrderStatusEnum[]{OrderStatusEnum.PendingDelivery, OrderStatusEnum.PublishingAuditingRejected});
            put(OrderOperationEnum.AutoDelivery, new OrderStatusEnum[]{OrderStatusEnum.Delivering, OrderStatusEnum.Delivering});
            put(OrderOperationEnum.Cancel, new OrderStatusEnum[]{OrderStatusEnum.Canceled, OrderStatusEnum.Canceled});
            put(OrderOperationEnum.TakeOff, new OrderStatusEnum[]{OrderStatusEnum.TakeOff, OrderStatusEnum.TakeOff});
            put(OrderOperationEnum.Finish, new OrderStatusEnum[]{OrderStatusEnum.Finished, OrderStatusEnum.Finished});
        }
    };

    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        //更新订单状态
        OrderOperationEnum operationEnum = orderOperationLog.getOrderOperationEnum();
        OrderStatusEnum orderStatusEnum = orderOperationEnumMapping.get(operationEnum)[orderOperationLog.isSuccessed() ? 0 : 1];
        OrderInfo orderInfo = orderOperationLog.getOrderInfo();
        orderInfo.setOrderStatus(orderStatusEnum.getValue());

        LocalDate date = new LocalDate(orderInfo.getStartTime());
        if(orderStatusEnum.equals(OrderStatusEnum.PendingDelivery)&&(date.compareTo(LocalDate.now()) <=0)) {
            orderInfo.setOrderStatus(OrderStatusEnum.Delivering.getValue());
            orderInfo.setEffectiveStartTime(DateUtils.getDateAccurateToMinute(new Date()));
            orderOperationLog.setOperation(OrderOperationEnum.AutoDelivery.getValue());
            orderDailyStoreMonitorStaticMapper.createOrderDailyStoreMonitorStatic(new OrderDateDto(orderInfo.getId(),new LocalDate().toDateTimeAtStartOfDay().toDate()));
        }
        orderInfoRepository.saveAndFlush(orderInfo);
        orderOperationLog.setOrderInfo(orderInfo);
    }
}
