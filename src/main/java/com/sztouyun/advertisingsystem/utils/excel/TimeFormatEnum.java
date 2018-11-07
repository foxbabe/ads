package com.sztouyun.advertisingsystem.utils.excel;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.EnumMessage;

import java.text.SimpleDateFormat;

/**
 * Created by wenfeng on 2018/3/15.
 */
public enum TimeFormatEnum implements EnumMessage<Integer> {
    Second(1,Constant.DATE_TIME_CN,"yyyy年MM月dd日 HH:mm:ss"),
    Minute(2,Constant.TIME_YMDHM_CN,"yyyy年MM月dd日 HH:mm"),
    Default(3,Constant.DATA_YMD_CN,"yyyy年MM月dd日"),
    Month(4,Constant.DATA_YM_CN,"yyyy年MM月");
    private Integer value;
    private String outputFormatPattern;
    private String displayName;
    TimeFormatEnum(Integer value, String outputFormatPattern, String displayName) {
        this.value = value;
        this.outputFormatPattern = outputFormatPattern;
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

    public String getOutputFormatPattern() {
        return outputFormatPattern;
    }
    public SimpleDateFormat getSimpleDateFormat(){
        return  new SimpleDateFormat(outputFormatPattern);
    }
}
