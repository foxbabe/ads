package com.sztouyun.advertisingsystem.service.rule.base.data;

import java.util.Date;

public class RuleInfo<TResult> implements IResult<TResult> {
    private String objectId;
    private int ruleType;
    private TResult result;
    //标准值
    private Object standard;
    //单位
    private Integer unit;

    //比较类型
    private Integer comparisonType;
    //扩展数据
    private String extendData;
    private Date createdTime = new Date();

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getRuleType() {
        return ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public TResult getResult() {
        return result;
    }

    public void setResult(TResult result) {
        this.result = result;
    }

    public String getExtendData() {
        return extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public <T> T getStandard() {
        return (T)standard;
    }

    public void setStandard(Object standard) {
        this.standard = standard;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(Integer comparisonType) {
        this.comparisonType = comparisonType;
    }
}


