package com.sztouyun.advertisingsystem.common.operation.rule.service;

import lombok.experimental.var;

import java.util.List;

public abstract class BaseValidationRuleOperationService<TData>  extends BaseRuleOperationService<TData,Boolean>{
    @Override
    protected Boolean getFinalResult(List<Boolean> resultList) {
        for (var result:resultList){
            if(result ==null || !result)
                return false;
        }
        return true;
    }
}
