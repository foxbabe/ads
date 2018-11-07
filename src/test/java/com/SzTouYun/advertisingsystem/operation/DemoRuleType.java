package com.SzTouYun.advertisingsystem.operation;

import com.sztouyun.advertisingsystem.common.operation.rule.data.IRuleType;

/**
 * Created by RiberLi on 2018/8/18 0018.
 */
public enum DemoRuleType implements IRuleType {
    IsTest(1,"IsTest"),
    CalcAdsProfit(2,"CalcAdsProfit");

    private Integer value;
    private String displayName;

    DemoRuleType(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
