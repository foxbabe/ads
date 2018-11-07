package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.order.OrderMaterial;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.model.order.QOrderMaterial;
import com.sztouyun.advertisingsystem.repository.order.OrderMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderMaterialOperation  implements IActionOperation<OrderOperationLog> {

    @Autowired
    private OrderMaterialRepository orderMaterialRepository;

    private final static QOrderMaterial qOrderMaterial = QOrderMaterial.orderMaterial;

    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        PartnerMaterial partnerMaterial = orderOperationLog.getPartnerMaterial();
        OrderMaterial orderMaterial = orderMaterialRepository.findOne(q->q.selectFrom(qOrderMaterial)
                .where(qOrderMaterial.orderId.eq(orderOperationLog.getOrderId()).and(qOrderMaterial.advertisementPositionCategory.eq(partnerMaterial.getAdvertisementPositionCategory()))));
        orderMaterial.setPartnerMaterialId(partnerMaterial.getId());
        orderMaterialRepository.save(orderMaterial);
    }
}
