package com.sztouyun.advertisingsystem.service.rule.base;

import com.sztouyun.advertisingsystem.service.rule.base.data.BaseRuleData;

public abstract class BaseValidationOperationService<
        TRuleData extends BaseRuleData>
        extends BaseRuleOperationService<TRuleData,Boolean> {

}
