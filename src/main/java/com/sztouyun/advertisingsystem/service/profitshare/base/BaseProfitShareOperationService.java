package com.sztouyun.advertisingsystem.service.profitshare.base;

import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareRuleData;
import com.sztouyun.advertisingsystem.service.rule.base.BaseRuleOperationService;

public abstract class BaseProfitShareOperationService<
        TRuleData extends ProfitShareRuleData,TResult>
        extends BaseRuleOperationService<TRuleData,TResult> {


}
