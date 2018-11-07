package com.sztouyun.advertisingsystem.service.rule.base.data;

import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRuleData<TRuleInfo>{
    private String objectId;
    private Map<RuleTypeEnum,Object> ruleFactValueMap = new HashMap<>();
    private List<TRuleInfo> ruleInfoList = new ArrayList<>();

    public BaseRuleData(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public BaseRuleData<TRuleInfo> addRuleFactValue(RuleTypeEnum ruleType, Object standard){
        ruleFactValueMap.put(ruleType,standard);
        return this;
    }

    public <TStandard> TStandard getRuleFactValue(RuleTypeEnum ruleType){
        return (TStandard)ruleFactValueMap.get(ruleType);
    }

    public void  addRuleInfo(TRuleInfo ruleInfo){
        ruleInfoList.add(ruleInfo);
    }

    public List<TRuleInfo> getRuleInfoList(){
        return ruleInfoList;
    }
}
