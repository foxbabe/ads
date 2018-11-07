package com.sztouyun.advertisingsystem.service.monitor;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.monitor.OrderDailyStoreMonitorStatic;
import com.sztouyun.advertisingsystem.model.monitor.QOrderDailyStoreMonitorStatic;
import com.sztouyun.advertisingsystem.repository.monitor.OrderDailyStoreMonitorStaticRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerOrderDailyDeliveryStatisticRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wenfeng on 2018/3/8.
 */
@Service
public class OrderDailyStoreMonitorStaticService extends BaseService {
    @Autowired
    private OrderDailyStoreMonitorStaticRepository orderDailyStoreMonitorStaticRepository;

    private static final QOrderDailyStoreMonitorStatic qOrderDailyStoreMonitorStatic= QOrderDailyStoreMonitorStatic.orderDailyStoreMonitorStatic;

    public List<OrderDailyStoreMonitorStatic> getOrderDailyStoreMonitorStaticList(PartnerOrderDailyDeliveryStatisticRequest request){
        return orderDailyStoreMonitorStaticRepository.findAll(q->q.select(qOrderDailyStoreMonitorStatic).from(qOrderDailyStoreMonitorStatic).where(qOrderDailyStoreMonitorStatic.orderId.eq(request.getOrderId()).and(qOrderDailyStoreMonitorStatic.date.goe(request.getStartDate())).and(qOrderDailyStoreMonitorStatic.date.lt(new LocalDate(request.getEndDate()).plusDays(1).toDate()))).orderBy(qOrderDailyStoreMonitorStatic.date.asc()));
    }
}
