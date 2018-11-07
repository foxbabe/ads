package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterialStatusEnum;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.model.order.QOrderMaterial;
import com.sztouyun.advertisingsystem.repository.order.OrderMaterialRepository;
import com.sztouyun.advertisingsystem.service.material.PartnerMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateOrderMaterialStatusOperation implements IActionOperation<OrderOperationLog> {

    @Autowired
    private PartnerMaterialService partnerMaterialService;
    @Autowired
    private OrderMaterialRepository orderMaterialRepository;

    private final QOrderMaterial qOrderMaterial =QOrderMaterial.orderMaterial;

    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        PartnerMaterial partnerMaterial = partnerMaterialService.getPartnerMaterial(orderOperationLog.getPartnerId(),orderOperationLog.getThirdPartMaterialId());
        if(!partnerMaterial.getMaterialStatus().equals(PartnerMaterialStatusEnum.Approved.getValue()))
            throw new BusinessException("素材未审核通过");
        if(!orderMaterialRepository.exists(qOrderMaterial.orderId.eq(orderOperationLog.getOrderId()).and(qOrderMaterial.advertisementPositionCategory.eq(partnerMaterial.getAdvertisementPositionCategory()))))
            throw new BusinessException("素材投放位置不匹配");
        orderOperationLog.setPartnerMaterial(partnerMaterial);
    }
}
