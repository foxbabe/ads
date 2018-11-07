package com.sztouyun.advertisingsystem.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by szty on 2018/6/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetConfig {
    List<SheetHeader> currentHeaders;
    private Map<String,TimeFormatEnum> timeFormatMap=null;
    private Map<String,BooleanTypeEnum> booleanTypeMap=null;
    private List<String> currencyKeys=null;
    private String sheetNameTemplate="第%d页";

    public SheetConfig(List<SheetHeader> currentHeaders, Map<String, TimeFormatEnum> timeFormatMap) {
        this.currentHeaders = currentHeaders;
        this.timeFormatMap = timeFormatMap;
    }
    public SheetConfig(List<SheetHeader> currentHeaders, Map<String, TimeFormatEnum> timeFormatMap,Map<String,BooleanTypeEnum> booleanTypeMap) {
        this.currentHeaders = currentHeaders;
        this.timeFormatMap = timeFormatMap;
        this.booleanTypeMap=booleanTypeMap;
    }

    public SheetConfig(List<SheetHeader> currentHeaders) {
        this(currentHeaders,new HashMap<>());

    }

    public SheetConfig addCurrencyKeys(List<String> currencyKeys){
        this.currencyKeys=currencyKeys;
        return this;
    }
}
