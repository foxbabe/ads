package com.sztouyun.advertisingsystem.service.profitshare.operations.base;

import com.sztouyun.advertisingsystem.service.profitshare.operations.data.StoreValidationRuleData;
import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.StoreValidationRuleInfo;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;

public abstract class BaseStoreProfitShareValidationOperation
        extends BaseProfitShareValidationOperation<StoreValidationRuleData,StoreValidationRuleInfo> {
    @Override
    protected StoreValidationRuleInfo getRuleInfo(StoreValidationRuleData ruleData, RuleConfig ruleConfig) {
        StoreValidationRuleInfo ruleInfo = new StoreValidationRuleInfo();
        ruleInfo.setStoreId(ruleData.getStore().getId());
        return ruleInfo;
    }
}
