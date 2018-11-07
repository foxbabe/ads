package com.sztouyun.advertisingsystem.common.operation.rule;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleResult;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleType;
import com.sztouyun.advertisingsystem.common.operation.rule.data.RuleResult;
import com.sztouyun.advertisingsystem.utils.ReflectUtil;

public abstract class BaseRuleOperation<TData,TResult,TRuleResultInfo> implements IOperation<TData,IRuleResult<TResult>> {

    @Override
    public final IRuleResult<TResult> operate(TData data) {
        TRuleResultInfo ruleResultInfo = ReflectUtil.newGenericInstance(this.getClass(),2);
        return new RuleResult<>(getResult(data,ruleResultInfo),getRuleType(data),ruleResultInfo);
    }

    protected abstract IRuleType getRuleType(TData data);

    protected abstract TResult getResult(TData data, TRuleResultInfo ruleResultInfo);
}
