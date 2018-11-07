package com.sztouyun.advertisingsystem.service.material;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.order.OrderMaterial;
import com.sztouyun.advertisingsystem.model.order.QOrderMaterial;
import com.sztouyun.advertisingsystem.repository.order.OrderMaterialRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wenfeng on 2018/3/8.
 */
@Service
public class OrderMaterialService extends BaseService {
    @Autowired
    private OrderMaterialRepository orderMaterialRepository;
    private final static QOrderMaterial qOrderMaterial=QOrderMaterial.orderMaterial;

    public OrderMaterial findOrderMaterialByOrderId(String orderId){
        OrderMaterial orderMaterial=orderMaterialRepository.findOne(qOrderMaterial.orderId.eq(orderId));
        if(orderMaterial==null)
            throw new BusinessException("订单ID无效");
        return orderMaterial;
    }
}
