package com.sztouyun.advertisingsystem.common.operation.rule.data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RuleResult<TResult> implements IRuleResult<TResult>{
    public RuleResult(TResult result, IRuleType ruleType, Object ruleResultInfo) {
        this.result = result;
        this.ruleType = ruleType;
        this.ruleResultInfoMap = new HashMap<IRuleType,Object>(){{put(ruleType,ruleResultInfo);}};
    }

    public RuleResult(TResult result, Map<IRuleType, Object> ruleResultInfoMap) {
        this.result = result;
        this.ruleResultInfoMap = ruleResultInfoMap;
    }

    private TResult result;

    private IRuleType ruleType;

    Map<IRuleType,Object> ruleResultInfoMap;
}
