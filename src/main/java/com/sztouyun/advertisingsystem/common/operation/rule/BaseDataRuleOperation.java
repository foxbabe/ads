package com.sztouyun.advertisingsystem.common.operation.rule;

import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleData;

public abstract class BaseDataRuleOperation<TData extends IRuleData,TResult,TRuleResultInfo> extends BaseRuleOperation<TData,TResult,TRuleResultInfo> {
    protected <T> T getRuleFactValue(TData data){
        return data.getRuleResultInfo(getRuleType(data));
    }
}
