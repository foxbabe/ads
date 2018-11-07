package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UpdateOrderEffectiveStartTimeOperation implements IActionOperation<OrderInfo> {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Override
    public void operateAction(OrderInfo orderInfo) {
        orderInfo.setEffectiveStartTime(LocalDate.now().toDate());
        orderInfoRepository.save(orderInfo);
    }
}
