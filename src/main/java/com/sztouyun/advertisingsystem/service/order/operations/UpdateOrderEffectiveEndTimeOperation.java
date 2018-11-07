package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import lombok.experimental.var;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class UpdateOrderEffectiveEndTimeOperation implements IActionOperation<OrderInfo> {

    @Autowired
    private OrderInfoRepository orderInfoRepository;


    @Override
    public void operateAction(OrderInfo orderInfo) {
        if(orderInfo.getEffectiveStartTime()==null)
            return;
        orderInfo.setEffectiveEndTime(new LocalDate().toDateTimeAtStartOfDay().minusSeconds(1).toDate());
        if(orderInfo.getOrderStatus().equals(OrderStatusEnum.TakeOff.getValue()))
            orderInfo.setEffectiveEndTime(DateUtils.getDateAccurateToMinute(new Date()));
        int effectiveTotalDays = 0 ;
        for (var orderDetail:orderInfo.getOrderDetails()){
            effectiveTotalDays = effectiveTotalDays + orderDetail.getOrderDetailDates().stream() .filter(q -> q.getDate().compareTo(orderInfo.getEffectiveEndTime()) <= 0)
                    .filter(q -> q.getDate().compareTo(new LocalDate(orderInfo.getEffectiveStartTime()).toDateTimeAtStartOfDay().toDate()) >= 0)
                    .collect(Collectors.toList()).size();
        }
        orderInfo.setEffectiveTotalDays(effectiveTotalDays);
        orderInfoRepository.save(orderInfo);
    }
}
