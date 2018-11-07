package com.sztouyun.advertisingsystem.service.profitshare.operations.base;

import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.ProfitShareCalculationRuleInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareCalculationData;
import com.sztouyun.advertisingsystem.service.rule.base.Operations.BaseRuleOperation;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;

public abstract class BaseProfitShareCalculationOperation
        extends BaseRuleOperation<ProfitShareCalculationData,Double,ProfitShareCalculationRuleInfo> {
    @Override
    protected ProfitShareCalculationRuleInfo getRuleInfo(ProfitShareCalculationData ruleData, RuleConfig ruleConfig) {
        ProfitShareCalculationRuleInfo ruleInfo = new ProfitShareCalculationRuleInfo();
        ruleInfo.setAdvertisementId(ruleData.getAdvertisement().getId());
        ruleInfo.setStoreId(ruleData.getStore().getId());
        ruleInfo.setDateTime(ruleData.getDateTime());
        return ruleInfo;
    }

    @Override
    protected RuleConfig getRuleConfig(ProfitShareCalculationData ruleData) {
        return new RuleConfig();
    }
}
