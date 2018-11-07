package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.mapper.OrderMapper;
import com.sztouyun.advertisingsystem.model.monitor.PartnerAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.repository.monitor.PartnerAdvertisementMonitorStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderMonitorOperation implements IActionOperation<OrderInfo> {

    @Autowired
    private PartnerAdvertisementMonitorStatisticRepository partnerAdvertisementMonitorStatisticRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void operateAction(OrderInfo orderInfo) {
        Integer totalDisplayTimes = orderInfo.getOrderMaterials().get(0).getDisplayTimes() * (orderMapper.getTotalStoreCountWithDate(orderInfo.getId()).intValue());
        PartnerAdvertisementMonitorStatistic partnerAdvertisementMonitorStatistic = new PartnerAdvertisementMonitorStatistic(orderInfo.getId(), totalDisplayTimes, orderInfo.getPartnerId());
        partnerAdvertisementMonitorStatisticRepository.save(partnerAdvertisementMonitorStatistic);
    }
}
