package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by wenfeng on 2017/8/7.
 */
public class StoreCategoryStandardDto extends StoreCategoryStandard {
    public  String leftComparisonTypeValue=null;

    public  String rightComparisonTypeValue=null;

    public String getLeftComparisonTypeValue() {
        return leftComparisonTypeValue;
    }

    public void setLeftComparisonTypeValue(String leftComparisonTypeValue) {
        this.leftComparisonTypeValue = leftComparisonTypeValue;
    }

    public String getRightComparisonTypeValue() {
        return rightComparisonTypeValue;
    }

    public void setRightComparisonTypeValue(String rightComparisonTypeValue) {
        this.rightComparisonTypeValue = rightComparisonTypeValue;
    }
}