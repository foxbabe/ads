package com.sztouyun.advertisingsystem.service.rule.base.data;

public class ValidationRuleInfo extends RuleInfo<Boolean> {
    //实际值
    private Object fact;

    public <T> T getFact() {
        return (T)fact;
    }

    public void setFact(Object fact) {
        this.fact = fact;
    }
}
