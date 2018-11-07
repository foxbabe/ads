package com.sztouyun.advertisingsystem.service.rule.base;

import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.service.rule.base.data.BaseRuleData;


public abstract  class BaseRuleOperationService<
        TRuleData extends BaseRuleData,TResult>
        extends BaseOperationService<TRuleData,TResult> {
}
