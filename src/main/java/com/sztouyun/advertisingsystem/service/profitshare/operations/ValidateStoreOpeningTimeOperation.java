package com.sztouyun.advertisingsystem.service.profitshare.operations;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.service.profitshare.operations.base.BaseStoreProfitShareValidationOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.StoreValidationRuleData;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class ValidateStoreOpeningTimeOperation extends BaseStoreProfitShareValidationOperation {
    @Override
    protected RuleTypeEnum getRuleType(StoreValidationRuleData ruleData) {
        return RuleTypeEnum.ValidateStoreOpeningTime;
    }


    @Override
    protected Double getFactValue(StoreValidationRuleData ruleData) {
        throw new BusinessException("门店营业时间不能为空！");
    }
}
