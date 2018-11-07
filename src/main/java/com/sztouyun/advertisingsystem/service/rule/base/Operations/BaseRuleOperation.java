package com.sztouyun.advertisingsystem.service.rule.base.Operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import com.sztouyun.advertisingsystem.service.rule.base.data.BaseRuleData;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleInfo;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;

public abstract class BaseRuleOperation<
        TRuleData extends BaseRuleData<TRuleInfo>,
        TResult,
        TRuleInfo extends RuleInfo<TResult>>
        implements IOperation<TRuleData,TResult> {

    protected abstract RuleTypeEnum getRuleType(TRuleData ruleData);
    protected abstract RuleConfig getRuleConfig(TRuleData ruleData);
    protected abstract TRuleInfo getRuleInfo(TRuleData ruleData,RuleConfig ruleConfig);
    protected abstract TResult executeRule(TRuleData ruleData, RuleConfig ruleConfig);


    @Override
    public TResult operate(TRuleData ruleData) {
        RuleConfig ruleConfig = getRuleConfig(ruleData);
        TResult result = executeRule(ruleData,ruleConfig);
        if(recordRuleInfo()){
            TRuleInfo ruleInfo = getRuleInfo(ruleData,ruleConfig);
            fillRuleInfo(ruleInfo,ruleData,ruleConfig,result);
            ruleData.addRuleInfo(ruleInfo);
            saveRuleInfo(ruleData,ruleInfo);
        }
        return result;
    }

    protected boolean recordRuleInfo(){
        return true;
    }

    protected void fillRuleInfo(TRuleInfo ruleInfo,TRuleData ruleData,RuleConfig ruleConfig,TResult result){
        ruleInfo.setObjectId(ruleData.getObjectId());
        ruleInfo.setRuleType(getRuleType(ruleData).getValue());
        ruleInfo.setResult(result);
        ruleInfo.setComparisonType(ruleConfig.getComparisonType());
        ruleInfo.setStandard(ruleConfig.getStandard());
        ruleInfo.setUnit(ruleConfig.getUnit());
        Object extendObject =getExtendObject(ruleData,ruleConfig);
        if(extendObject != null){
            ruleInfo.setExtendData(ObjectMapperUtils.toJsonString(extendObject));
        }
    }

    protected Object getExtendObject(TRuleData ruleData,RuleConfig ruleConfig){
        return null;
    }

    protected void saveRuleInfo(TRuleData ruleData,TRuleInfo ruleInfo){
    }
}
