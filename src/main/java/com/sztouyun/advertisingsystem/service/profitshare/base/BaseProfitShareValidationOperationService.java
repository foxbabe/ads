package com.sztouyun.advertisingsystem.service.profitshare.base;

import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareRuleData;
import com.sztouyun.advertisingsystem.service.rule.base.BaseValidationOperationService;

public abstract class BaseProfitShareValidationOperationService<
        TRuleData extends ProfitShareRuleData>
        extends BaseValidationOperationService<TRuleData> {

}
