package com.sztouyun.advertisingsystem.common.operation.rule.service;

import com.sztouyun.advertisingsystem.utils.MathUtil;

import java.util.List;

public abstract class BaseCalculationRuleOperationService<TData,TResult extends Number>  extends BaseRuleOperationService<TData,TResult>{
    @Override
    protected TResult getFinalResult(List<TResult> resultList) {
        return MathUtil.sum(resultList);
    }
}
