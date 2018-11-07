package com.sztouyun.advertisingsystem.service.profitshare.operations.base;

import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfig;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.ProfitShareValidationRuleInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareRuleData;
import com.sztouyun.advertisingsystem.service.rule.base.Operations.BaseValidationOperation;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseProfitShareValidationOperation<
        TRuleData extends ProfitShareRuleData<TRuleInfo>,
        TRuleInfo extends ProfitShareValidationRuleInfo>
        extends BaseValidationOperation<TRuleData,TRuleInfo,Double> {

    @Autowired
    private HistoricalParamConfigService advertisementProfitShareConfigService;

    @Override
    protected void fillRuleInfo(TRuleInfo ruleInfo, TRuleData ruleData, RuleConfig ruleConfig, Boolean result) {
        ruleInfo.setDateTime(ruleData.getDateTime());
        super.fillRuleInfo(ruleInfo, ruleData, ruleConfig, result);
    }

    @Override
    protected RuleConfig getRuleConfig(TRuleData ruleData) {
        HistoricalParamConfig config = advertisementProfitShareConfigService.getHistoricalParamConfigFromCache(getRuleType(ruleData),ruleData.getDateTime());
        return new RuleConfig(config.getValue(),config.getUnit(),config.getComparisonType());
    }
}
