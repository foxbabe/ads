package com.sztouyun.advertisingsystem.utils;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created by RiberLi on 2018/8/15 0015.
 */
public class MathUtil {
    private static final Random RANDOM = new Random();

    public static int randomInt(int bound){
        return RANDOM.nextInt(bound);
    }

    public static <T extends Number> T sum(T... values){
        BigDecimal total = new BigDecimal(0);
        Class<T> clazz =null;
        for (T value: values){
            clazz= (Class<T>)value.getClass();
            total = total.add(new BigDecimal(value.toString()));
        }
        return NumberFormatUtil.getNumber(total.toString(),clazz);
    }

    public static <T extends Number> T sum(List<T> values){
        if(CollectionUtils.isEmpty(values))
            return null;
        BigDecimal total = new BigDecimal(0);
        Class<T> clazz =null;
        for (T value: values){
            clazz= (Class<T>)value.getClass();
            total = total.add(new BigDecimal(value.toString()));
        }
        return NumberFormatUtil.getNumber(total.toString(),clazz);
    }
}
