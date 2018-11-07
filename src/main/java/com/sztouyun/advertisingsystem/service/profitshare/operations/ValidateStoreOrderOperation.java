package com.sztouyun.advertisingsystem.service.profitshare.operations;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.service.profitshare.operations.base.BaseStoreProfitShareValidationOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.StoreValidationRuleData;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class ValidateStoreOrderOperation extends BaseStoreProfitShareValidationOperation {
    @Override
    protected RuleTypeEnum getRuleType(StoreValidationRuleData ruleData) {
        return RuleTypeEnum.ValidateStoreOrder;
    }

    @Override
    protected Double getFactValue(StoreValidationRuleData ruleData) {
        throw new BusinessException("订单数量不能空！");
    }
}
