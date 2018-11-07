package com.sztouyun.advertisingsystem.common.operation.rule.data;

import java.util.Map;

public interface IRuleData {
    Map<IRuleType,Object> getRuleResultInfoMap();
    default <TResultInfo> TResultInfo getRuleResultInfo(IRuleType ruleType){
        return (TResultInfo) getRuleResultInfoMap().get(ruleType);
    }

    default void addRuleResultInfo(IRuleType ruleType,Object resultInfo){
        getRuleResultInfoMap().put(ruleType,resultInfo);
    }
}
