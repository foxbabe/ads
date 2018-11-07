package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.monitor.OrderDateDto;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/9.
 */
public interface OrderDailyStoreMonitorStaticMapper {
    void createOrderDailyStoreMonitorStatic(OrderDateDto param);

    void updateOrderDailyAvailableStoreMonitorStatic(Date date);
}
