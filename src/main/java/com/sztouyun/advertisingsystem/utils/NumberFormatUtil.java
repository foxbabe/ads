package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by wenfeng on 2017/9/14.
 */
public class NumberFormatUtil {
    public static Double format(Double src,Integer scale){
        return new BigDecimal(src).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static Double format(Double src){
        return new BigDecimal(src).setScale(Constant.SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal format(BigDecimal src,Integer scale){
        return src.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static Double formatDouble(String src,Integer scale){
        return new BigDecimal(src).setScale(scale, RoundingMode.DOWN).doubleValue();
    }

    /**
     * 精确到小数点后XX位，不做四舍五入，超过XX位舍去
     * 为0的时候，显示0而不是0.00
     */
    public static String format(Double src,String scale){
        if(src==null||src==0D)
            return "0";
        DecimalFormat df = new DecimalFormat(scale);
        df.setRoundingMode(RoundingMode.FLOOR);
        return df.format(src).toString();
    }

    /**
     *精确到小数点后XX位，超过XX位舍去, 是否四舍五入由参数 roundingMode 控制
     * 为0的时候，显示0而不是0.00
     */
    public static String format(Double src,String scale, RoundingMode roundingMode ){
        if(src==null||src==0D)
            return "0";
        DecimalFormat df = new DecimalFormat(scale);
        df.setRoundingMode(roundingMode);
        return df.format(src).toString();
    }


    public static String formatAbandon(Double src,Integer scale){
        if(src != null && src > 0)
            return new BigDecimal(src).setScale(scale, BigDecimal.ROUND_DOWN).toString();
        return "0";
    }

    public static BigDecimal format(BigDecimal src){
        return src.setScale(Constant.SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 数字格式化（分子/分母）
     * @param numerator Object[Long/Double]
     * @param denominator Object[Long/Double]
     * @param pattern
     * @return
     */
    public static String format(Object numerator ,Object denominator, String pattern){
        BigDecimal numeratorDecimal = null;
        BigDecimal denominatorDecimal = null;
        if(numerator instanceof Long && denominator instanceof Long){
            if((Long)numerator <=0 || (Long)denominator<=0)
                return Constant.ZERO_PERCENT;
            if(numerator.equals(denominator))
                return Constant.MAX_PERCENT;
            numeratorDecimal = new BigDecimal((Long)numerator);
            denominatorDecimal = new BigDecimal((Long)denominator);
        }else if(numerator instanceof Double && denominator instanceof Double){
            if((Double)numerator <=0 || (Double)denominator<=0)
                return Constant.ZERO_PERCENT;
            if(numerator.equals(denominator))
                return Constant.MAX_PERCENT;
            numeratorDecimal = new BigDecimal((Double)numerator);
            denominatorDecimal = new BigDecimal((Double)denominator);
        }
        if(numeratorDecimal != null && denominatorDecimal != null){
            Integer scale=pattern.length()-1;
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.applyPattern(pattern);

            BigDecimal resultDecimal=numeratorDecimal.divide(denominatorDecimal,scale, BigDecimal.ROUND_DOWN);
            String result= decimalFormat.format(resultDecimal.doubleValue());
            if(result.equals(pattern))
                return Constant.ZERO_PERCENT;
            return result;
        }
        return "";
    }

    public static String formatToString(BigDecimal src){
        return src.setScale(Constant.SCALE, BigDecimal.ROUND_HALF_UP).toString();
    }

    public  static  <T extends Number> T getNumber(String number,Class<T> numberClass){
        switch (numberClass.getCanonicalName()){
            case "java.lang.Integer":
                if(number.indexOf(Constant.POINT)!=-1){
                    number=number.substring(0,number.indexOf(Constant.POINT));
                }
                return (T) Integer.valueOf(number);
            case "java.lang.Long":
                if(number.indexOf(Constant.POINT)!=-1){
                    number=number.substring(0,number.indexOf(Constant.POINT));
                }
                return (T) Long.valueOf(number);
            case "java.lang.Float":
                return (T) Float.valueOf(number);
            case "java.lang.Byte":
                return (T) Byte.valueOf(number);
            case "java.lang.Short":
                return (T) Short.valueOf(number);
            case "java.lang.Double":
                return (T) Double.valueOf(number);
            default:
                throw new BusinessException("unsupported type");
        }
    }


}
