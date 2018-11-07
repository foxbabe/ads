package com.sztouyun.advertisingsystem.utils.excel;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by szty on 2018/7/23.
 */
public enum  BooleanTypeEnum implements EnumMessage<Integer> {
    Default(1,"是,否"),
    HasOrNot(2,"有,无")
    ;
    private Integer value;
    private String displayName;
    private String[]  booleanTypeNames;
    private String defaultBooleanTypeName;
    BooleanTypeEnum(Integer value,String displayName ){
        this.value=value;
        this.displayName=displayName;
        this.booleanTypeNames=displayName.split(Constant.SEPARATOR);
        this.defaultBooleanTypeName =booleanTypeNames[1];
    }
    BooleanTypeEnum(Integer value,String displayName,String defaultBooleanTypeName ){
        this.value=value;
        this.displayName=displayName;
        this.booleanTypeNames=displayName.split(Constant.SEPARATOR);
        this.defaultBooleanTypeName =defaultBooleanTypeName;
    }
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getBooleanName(Boolean booleanValue){
        if(booleanValue==null)
            return defaultBooleanTypeName;
        return booleanValue?booleanTypeNames[0]:booleanTypeNames[1];
    }
}
