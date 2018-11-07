package com.sztouyun.advertisingsystem.common.operation.rule.service;

import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleResult;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleType;
import com.sztouyun.advertisingsystem.common.operation.rule.data.RuleResult;
import lombok.experimental.var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRuleOperationService<TData,TResult>  extends BaseOperationService<TData,IRuleResult<TResult>>{
    @Override
    protected final IRuleResult<TResult> getResult(List<IRuleResult<TResult>> operationResultList) {
        Map<IRuleType,Object> ruleResultInfoMap = new HashMap<>();
        var resultList =new ArrayList<TResult>();
        for (var operationResult :operationResultList){
            resultList.add(operationResult.getResult());
            ruleResultInfoMap.put(operationResult.getRuleType(),operationResult.getRuleResultInfo());
        }
        return new RuleResult<>(getFinalResult(resultList),ruleResultInfoMap);
    }

    protected abstract TResult getFinalResult(List<TResult> resultList);
}
