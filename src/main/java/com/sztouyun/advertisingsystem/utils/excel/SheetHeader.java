package com.sztouyun.advertisingsystem.utils.excel;

import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Created by fengwen on 15/11/2017.
 */
public class SheetHeader {
    /**
     * 是否要合并单元格
     */
    private boolean isNeedMerge=false;
    /**
     * 合并区域
     */
    private CellRangeAddress  cellRangeAddress;
    /**
     * 表头名字
     */
    private String[] headerNames;
    /**
     * 表头对应的KEY，只有最下级表头有对应的属性，用于从data取值填充表格
     */
    private String[] headerKeys;
    /**
     * 表头名称的对其方式
     */
    private Short alignType;

    private Boolean hasSortNumber=true;

    public boolean isNeedMerge() {
        return isNeedMerge;
    }

    public void setNeedMerge(boolean needMerge) {
        isNeedMerge = needMerge;
    }

    public CellRangeAddress getCellRangeAddress() {
        return cellRangeAddress;
    }

    public void setCellRangeAddress(CellRangeAddress cellRangeAddress) {
        this.cellRangeAddress = cellRangeAddress;
    }

    public String[] getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(String[] headerNames) {
        this.headerNames = headerNames;
    }

    public String[] getHeaderKeys() {
        return headerKeys;
    }

    public void setHeaderKeys(String[] headerKeys) {
        this.headerKeys = headerKeys;
    }

    public Short getAlignType() {
        return alignType;
    }

    public void setAlignType(Short alignType) {
        this.alignType = alignType;
    }

    public SheetHeader(Integer[] mergeArea, String[] headerNames, String[] headerKeys, Short alignType){
        if(mergeArea!=null && mergeArea.length==4){
            cellRangeAddress=new CellRangeAddress(mergeArea[0],mergeArea[1],mergeArea[2],mergeArea[3]);
            isNeedMerge=true;
        }
        if(headerKeys!=null && headerKeys.length==headerNames.length){
            hasSortNumber=false;
        }
        this.headerKeys=headerKeys;
        this.headerNames=headerNames;
        this.alignType=alignType;
    }

    public Boolean getHasSortNumber() {
        return hasSortNumber;
    }

    public void setHasSortNumber(Boolean hasSortNumber) {
        this.hasSortNumber = hasSortNumber;
    }
}
