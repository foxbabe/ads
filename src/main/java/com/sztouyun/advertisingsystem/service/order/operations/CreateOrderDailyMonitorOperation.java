package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.mapper.OrderDailyStoreMonitorStaticMapper;
import com.sztouyun.advertisingsystem.viewmodel.monitor.OrderDateDto;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单每日的监控信息（order,date）
 */
@Service
public class CreateOrderDailyMonitorOperation  implements IActionOperation<OrderInfo> {
    @Autowired
    private OrderDailyStoreMonitorStaticMapper orderDailyStoreMonitorStaticMapper;
    @Override
    public void operateAction(OrderInfo orderInfo) {
        orderDailyStoreMonitorStaticMapper.createOrderDailyStoreMonitorStatic(new OrderDateDto(orderInfo.getId(),new LocalDate().toDateTimeAtStartOfDay().toDate()));

    }
}
