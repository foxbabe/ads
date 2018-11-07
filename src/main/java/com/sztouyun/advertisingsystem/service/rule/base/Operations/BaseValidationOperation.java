package com.sztouyun.advertisingsystem.service.rule.base.Operations;

import com.sztouyun.advertisingsystem.service.rule.base.data.BaseRuleData;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;
import com.sztouyun.advertisingsystem.service.rule.base.data.ValidationRuleInfo;
import com.sztouyun.advertisingsystem.utils.ComparisonUtil;

public abstract class BaseValidationOperation<
        TRuleData extends BaseRuleData<TRuleInfo>,
        TRuleInfo extends ValidationRuleInfo,
        TStandard>
        extends BaseRuleOperation<TRuleData,Boolean,TRuleInfo> {

    protected abstract TStandard getFactValue(TRuleData ruleData);

    @Override
    protected Boolean executeRule(TRuleData ruleData, RuleConfig ruleConfig) {
        return ComparisonUtil.compare((Double)getRuleFactValue(ruleData),(Double)ruleConfig.getStandard(),ruleConfig.getComparisonTypeEnum());
    }

    @Override
    protected void fillRuleInfo(TRuleInfo ruleInfo, TRuleData ruleData, RuleConfig ruleConfig, Boolean result) {
        ruleInfo.setFact(getRuleFactValue(ruleData));
        super.fillRuleInfo(ruleInfo, ruleData, ruleConfig, result);
    }

    private TStandard getRuleFactValue(TRuleData ruleData){
        TStandard ruleFactValue = ruleData.getRuleFactValue(getRuleType(ruleData));
        return ruleFactValue == null ? getFactValue(ruleData) : ruleFactValue;
    }
}
