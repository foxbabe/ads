package com.sztouyun.advertisingsystem.service.rule.base.data;

import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

public class RuleConfig{
    public RuleConfig() {
    }

    public RuleConfig(Object standard, Integer unit, Integer comparisonType) {
        this.standard = standard;
        this.unit = unit;
        this.comparisonType = comparisonType;
    }

    //标准值
    private Object standard;
    //单位
    private Integer unit;

    private Integer comparisonType;

    public Object getStandard() {
        return standard;
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

    public ComparisonTypeEnum getComparisonTypeEnum(){
        return EnumUtils.toEnum(getComparisonType(),ComparisonTypeEnum.class);
    }
}
