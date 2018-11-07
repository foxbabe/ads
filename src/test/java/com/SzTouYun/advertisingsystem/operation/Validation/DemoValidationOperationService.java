package com.SzTouYun.advertisingsystem.operation.Validation;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.common.operation.rule.service.BaseValidationRuleOperationService;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleResult;
import com.SzTouYun.advertisingsystem.operation.DemoRuleData;
import org.springframework.stereotype.Service;

@Service
public class DemoValidationOperationService extends BaseValidationRuleOperationService<DemoRuleData> {
    @Override
    protected void onOperating(DemoRuleData demoRuleData, IOperationCollection<DemoRuleData, IRuleResult<Boolean>> operationCollection) {
        operationCollection.add(ValidateIsTestOperation.class);
    }
}
