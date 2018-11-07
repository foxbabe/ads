package com.sztouyun.advertisingsystem.service.profitshare.operations;

import com.sztouyun.advertisingsystem.service.profitshare.operations.base.BaseProfitShareCalculationOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareCalculationData;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;
import org.springframework.stereotype.Service;

@Service
public class CalculateStoreAdvertisementProfitShareOperation extends BaseProfitShareCalculationOperation {
    @Override
    protected RuleTypeEnum getRuleType(ProfitShareCalculationData ruleData) {
        return RuleTypeEnum.CalculateStoreAdvertisementProfitShare;
    }

    @Override
    protected Double executeRule(ProfitShareCalculationData ruleData, RuleConfig ruleConfig) {
        return ruleData.getAdvertisement().getProfitShareStandardAmount();
    }
}
