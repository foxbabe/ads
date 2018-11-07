package com.SzTouYun.advertisingsystem.operation.Validation;

import com.sztouyun.advertisingsystem.common.operation.rule.BaseRuleOperation;
import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleType;
import com.SzTouYun.advertisingsystem.operation.DemoRuleData;
import com.SzTouYun.advertisingsystem.operation.DemoRuleResult;
import com.SzTouYun.advertisingsystem.operation.DemoRuleType;
import org.springframework.stereotype.Service;

@Service
public class ValidateIsTestOperation extends BaseRuleOperation<DemoRuleData,Boolean,DemoRuleResult> {

    @Override
    protected IRuleType getRuleType(DemoRuleData data) {
        return DemoRuleType.IsTest;
    }

    @Override
    protected Boolean getResult(DemoRuleData data, DemoRuleResult demoRuleResult) {
        return null;
    }
}
