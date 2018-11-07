package com.SzTouYun.advertisingsystem.operation.Calculation;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.common.operation.rule.service.BaseCalculationRuleOperationService;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleResult;
import com.SzTouYun.advertisingsystem.operation.DemoRuleData;
import org.springframework.stereotype.Service;

@Service
public class DemoCalculationOperationService extends BaseCalculationRuleOperationService<DemoRuleData,Double> {

    @Override
    protected void onOperating(DemoRuleData demoRuleData, IOperationCollection<DemoRuleData, IRuleResult<Double>> operationCollection) {
        operationCollection.add(CalcAdProfitOperation.class);
    }

}
