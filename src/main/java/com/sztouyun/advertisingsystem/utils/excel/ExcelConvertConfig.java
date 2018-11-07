package com.sztouyun.advertisingsystem.utils.excel;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.BaseImportInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by szty on 2018/5/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelConvertConfig extends BaseImportInfo {
    /**
     * 读取数据的起始行，默认去掉Excel第一行的表头
     */
    private Integer startRowIndex=1;
    /**
     * csv的字段分隔符
     */
    private String separator= Constant.SEPARATOR;
    /**
     * csv的换行符
     */
    private String newLine=Constant.NEW_LINE;
    /**
     * 指定写入Excel的指定列索引，空则写入所有列
     */
    private Integer[] filterCols ={};

    private String[] filterCellKeys ={};
    /**
     * 默认一次写入CSV 200行
     */
    private Integer batchSize=200;

    private String[] headerNames ={};

    private Map<String,Object> defaultValueMap=null;

    //金额相关字段（以分计算）
    private List<String> currencyKeys=null;

    public ExcelConvertConfig(String oid, String bid) {
        this(oid,bid,null);
    }

    public ExcelConvertConfig(String oid, String bid, Integer[] filterCols) {
        this(oid,bid, filterCols,null);
    }

    public ExcelConvertConfig(String oid, String bid, Integer[] filterCols, String[] headerNames) {
        this(oid,bid, filterCols,headerNames,null);
    }

    public ExcelConvertConfig(String oid, String bid, Integer[] filterCols, String[] headerNames, String [] filterCellKeys) {
        super(oid,bid);
        if(filterCols !=null){
            this.filterCols = filterCols;
        }
        if(headerNames!=null){
            this.headerNames=headerNames;
        }
        if(filterCellKeys !=null){
            this.filterCellKeys = filterCellKeys;
        }
    }

    public String getSavePath(){
        return FileUtils.getJarPath()+"contract"+ File.separator+getOid()+".csv";
    }

    public ExcelConvertConfig setHeaderNames(String[] headerNames) {
        this.headerNames = headerNames;
        return this;
    }

    public ExcelConvertConfig addCurrencyKeys(List<String> currencyKeys){
        if(currencyKeys!=null){
            this.currencyKeys=currencyKeys;
        }
        return this;
    }

    public ExcelConvertConfig configDefaultValue(Map<String,Object> defaultValueMap){
        this.defaultValueMap=defaultValueMap;
        return this;
    }

}
