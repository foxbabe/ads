package com.SzTouYun.advertisingsystem.operation.Calculation;

import com.sztouyun.advertisingsystem.common.operation.rule.BaseRuleOperation;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleType;
import com.SzTouYun.advertisingsystem.operation.DemoRuleData;
import com.SzTouYun.advertisingsystem.operation.DemoRuleResult;
import com.SzTouYun.advertisingsystem.operation.DemoRuleType;
import org.springframework.stereotype.Service;

@Service
public class CalcAdProfitOperation extends BaseRuleOperation<DemoRuleData,Double,DemoRuleResult> {
    @Override
    protected IRuleType getRuleType(DemoRuleData data) {
        return DemoRuleType.CalcAdsProfit;
    }

    @Override
    protected Double getResult(DemoRuleData data, DemoRuleResult demoRuleResult) {
        return 0D;
    }
}
