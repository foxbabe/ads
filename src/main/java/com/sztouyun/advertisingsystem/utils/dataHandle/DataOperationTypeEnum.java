package com.sztouyun.advertisingsystem.utils.dataHandle;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.utils.excel.EffectProfitHandleService;

/**
 * Created by fengwen on 23/08/2018.
 */
public enum DataOperationTypeEnum implements EnumMessage<Integer> {
    ExcelToCsv(1,"excel 2 csv",null),
    ExcelToMongo(2,"excel 2 mongo",EffectProfitHandleService.class);
    ;
    private Integer value;
    private String displayName;
    private String targetMethod;
    private Class<?> handler;

    DataOperationTypeEnum(Integer value, String displayName, Class<?> handler) {
        this.value = value;
        this.displayName = displayName;
        this.targetMethod="execute";
        this.handler = handler;
    }
    DataOperationTypeEnum(Integer value, String displayName,String targetMethod, Class<?> handler) {
        this.value = value;
        this.displayName = displayName;
        this.targetMethod=targetMethod;
        this.handler = handler;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Class<?> getHandler() {
        return handler;
    }

    public String getTargetMethod() {
        return targetMethod;
    }
}
