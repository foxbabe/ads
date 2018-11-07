package com.sztouyun.advertisingsystem.service.order.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateCoorperationPartnerOperation implements IActionOperation<OrderOperationLog> {

    @Autowired
    private CooperationPartnerService cooperationPartnerService;


    @Override
    public void operateAction(OrderOperationLog orderOperationLog) {
        CooperationPartner partner = cooperationPartnerService.findCooperationPartnerById(orderOperationLog.getPartnerId());
        if(partner==null)
            throw new BusinessException("合作方不存在");
        if(partner.isDisabled())
            throw new BusinessException("合作方被禁用,不能操作订单");
    }

}
