package com.sztouyun.advertisingsystem.common.operation.rule.data;

public interface IRuleResult<TResult> extends IRuleData{
    TResult getResult();
    IRuleType getRuleType();
    default Object getRuleResultInfo(){
        return getRuleResultInfo(getRuleType());
    }
}
