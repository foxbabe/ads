package com.sztouyun.advertisingsystem.utils.excel;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengwen on 15/11/2017.
 */
public class ExcelData {
    /**
     * 时间格式
     */
    private Map<String,TimeFormatEnum> timeFormatMap=new HashMap<>();
    /**
     * sheet名称
     */
    private String sheetName;
    /**
     * 表头数组，至少一个元素
     */
    private List<ExcelHeader> headers=new ArrayList<>();
    /**
     * 要填充的数据
     */
    private JSONArray jsonArray;

    /**
     * boolean类型展示配置
     * @return
     */
    private Map<String,BooleanTypeEnum> booleanTypeMap =new HashMap<>();

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<ExcelHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ExcelHeader> header) {
        this.headers = headers;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public ExcelData addData(List list) {
        this.jsonArray =new JSONArray(list);
        return this;
    }
    public ExcelData(String sheetName){
        this.sheetName=sheetName;
    }
    public ExcelData addTimeFormatConfig(Map<String,TimeFormatEnum> timeFormatMap){
        this.timeFormatMap=timeFormatMap;
        return this;
    }
    public ExcelData addBooleanTypeConfig(Map<String,BooleanTypeEnum> booleanTypeMap){
        this.booleanTypeMap =booleanTypeMap;
        return this;
    }
    public ExcelData addHeader(ExcelHeader excelHeader){
        headers.add(excelHeader);
        return this;
    }

    public Map<String, TimeFormatEnum> getTimeFormatMap() {
        return timeFormatMap;
    }

    public Map<String, BooleanTypeEnum> getBooleanTypeMap() {
        return booleanTypeMap;
    }
}
