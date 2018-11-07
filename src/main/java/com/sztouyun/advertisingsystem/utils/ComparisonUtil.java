package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;

public class ComparisonUtil {
    public static boolean compare(double source, double target, ComparisonTypeEnum comparisonType){
        switch (comparisonType){
            case EQ:
                return source == target;
            case GT:
                return source > target;
            case LT:
                return source < target;
            case GE:
                return source >= target;
            case LE:
                return source <= target;
        }
        return false;
    }
}
