package com.sztouyun.advertisingsystem.service.profitshare.operations.base;

import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.AdvertisementValidationRuleInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.AdvertisementValidationRuleData;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;

public abstract class BaseAdvertisementProfitShareValidationOperation
        extends BaseProfitShareValidationOperation<AdvertisementValidationRuleData,AdvertisementValidationRuleInfo> {

    @Override
    protected AdvertisementValidationRuleInfo getRuleInfo(AdvertisementValidationRuleData ruleData, RuleConfig ruleConfig) {
        AdvertisementValidationRuleInfo ruleInfo = new AdvertisementValidationRuleInfo();
        ruleInfo.setStoreId(ruleData.getStore().getId());
        ruleInfo.setAdvertisementId(ruleData.getAdvertisement().getId());
        return ruleInfo;
    }
}
