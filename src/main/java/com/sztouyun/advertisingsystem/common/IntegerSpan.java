package com.sztouyun.advertisingsystem.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegerSpan {
    private Integer startValue;
    private Integer endValue;

    /**
     * 判断两个区间是否重合, 例如 1 - 9 与 5 - 10 重合
     */
    public boolean hasIntersection(IntegerSpan integerSpan) {
        if(integerSpan.getEndValue() !=null &&  integerSpan.getEndValue() <getStartValue() )
            return false;
        if(getEndValue() !=null && integerSpan.getStartValue() >getEndValue())
            return false;
        return true;
    }

    /**
     * 是否为正整数
     */
    @JsonIgnore
    public boolean isPositiveInteger() {
        if(startValue == null)
            return false;
        if(startValue < 0)
            return false;
        if(endValue !=null && endValue<0)
            return false;
        return true;
    }

    /**
     * 是否是有效的区间
     */
    @JsonIgnore
    public boolean isValidSpan() {
        if(startValue == null)
            return false;
        if(endValue != null && endValue<startValue)
            return false;
        return true;
    }
}
