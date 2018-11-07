package com.sztouyun.advertisingsystem.utils.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.ExcelUtil;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by szty on 2018/6/11.
 */
@Data
public class MyWorkbook {
    Workbook wb = null;
    Sheet currentSheet = null;
    SheetConfig currentSheetConfig = null;
    Integer insertPosition=0;
    JSONArray data = new JSONArray();
    Integer sheetMaxRecordSize=Constant.SHEET_RECORD_SIZE;
    Integer currentSheetRecordSize=0;
    Integer currentSheetIndex=0;

    public MyWorkbook(Workbook wb) {
        this.wb = wb;
    }

    public MyWorkbook(Workbook wb, Integer sheetMaxRecordSize) {
        this(wb);
        if(sheetMaxRecordSize!=null){
            this.sheetMaxRecordSize = sheetMaxRecordSize;
        }
    }

    public void addData(List list, SheetConfig sheetConfig) {
        if(sheetConfig==null)
            throw new BusinessException("参数不正确");
        if(list==null){
            list=new ArrayList();
        }
        this.data = new JSONArray(list);
        this.currentSheetConfig = sheetConfig;
        if(currentSheetRecordSize==0 || this.currentSheetRecordSize+list.size()>this.sheetMaxRecordSize){
            this.currentSheetRecordSize=0;
            this.currentSheetIndex++;
            insertPosition = 0;
            currentSheet = wb.createSheet(String.format(sheetConfig.getSheetNameTemplate(),this.currentSheetIndex));
        }
        addSheetHeaderAndData();
        this.currentSheetRecordSize+=list.size();
    }

    private void addSheetHeaderAndData(){
        Map<String,TimeFormatEnum > timeFormatEnumMap=this.currentSheetConfig.getTimeFormatMap();
        Map<String,BooleanTypeEnum> booleanTypeEnumMap=this.currentSheetConfig.getBooleanTypeMap();
        List<String> currencyKeys=this.currentSheetConfig.getCurrencyKeys();
        int startRow=this.currentSheetConfig.getCurrentHeaders().size();
        //table对应的KEY在最下级的Header
        SheetHeader mainTableHeader=this.currentSheetConfig.getCurrentHeaders().get(startRow-1);
        String[] keys=mainTableHeader.getHeaderKeys();
        if(currentSheetRecordSize==0){
            this.insertPosition=startRow;
            setSheetHeader();
        }
        Row row=null;
        for(int i=0,line=data.size();i<line;i++){
            row = currentSheet.createRow(i + this.insertPosition);
            JSONObject jsonObject=data.getJSONObject(i);
            if(mainTableHeader.getHasSortNumber()){
                row.createCell(0).setCellValue(i+this.insertPosition);  //序号列
                for(int j=0,cel=keys.length;j<cel;j++){
                    row.createCell(j+1).setCellValue(filterValue(keys[j],jsonObject.get(keys[j]),timeFormatEnumMap,booleanTypeEnumMap,currencyKeys));
                }
            }else{
                for(int j=0,cel=keys.length;j<cel;j++){
                    row.createCell(j).setCellValue(filterValue(keys[j],jsonObject.get(keys[j]),timeFormatEnumMap,booleanTypeEnumMap,currencyKeys));
                }
            }

        }
        updateInsertPosition(row);
    }

    private void updateInsertPosition(Row row){
        if(row!=null){
            this.insertPosition=row.getRowNum()+1;
        }
    }

    private  void setSheetHeader() {
        this.currentSheet.setDefaultColumnWidth((short)15);
        List<SheetHeader> headers=this.currentSheetConfig.getCurrentHeaders();
        for(int i=0,len=headers.size();i<len;i++){
            SheetHeader header=headers.get(i);
            CellRangeAddress cellRangeAddress=header.getCellRangeAddress();
            if(cellRangeAddress!=null){
                currentSheet.addMergedRegion(cellRangeAddress);
            }
            Row row = currentSheet.createRow(i);
            String[] headerNames=header.getHeaderNames();
            for(int j=0,names=headerNames.length;j<names;j++){
                Cell cell = row.createCell((short) j);
                cell.setCellValue(headerNames[j]);
                cell.setCellStyle(getCellStyle(this.wb,header.getAlignType()));

            }
        }
    }

    private static CellStyle getCellStyle(Workbook wb, Short alignType){
        Font font=wb.createFont();
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short)11);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(alignType);
        style.setFont(font);
        return  style;
    }

    private static String filterValue(String key,Object src,Map<String,TimeFormatEnum> timeFormatEnumMap,Map<String,BooleanTypeEnum> booleanTypeEnumMap,List<String> currencyKeys){
        if(src==null)
            return "";
        if(!CollectionUtils.isEmpty(currencyKeys) && currencyKeys.contains(key)){
            return org.springframework.util.StringUtils.isEmpty(src.toString())?"0":
                    NumberFormatUtil.format(Double.valueOf(src.toString())/100,Constant.SCALE_TWO, RoundingMode.DOWN);
        }
        if(src instanceof java.lang.Boolean)
            return (booleanTypeEnumMap==null || !booleanTypeEnumMap.containsKey(key))?BooleanTypeEnum.Default.getBooleanName((boolean)src):(booleanTypeEnumMap.get(key).getBooleanName((boolean)src));
        if(src instanceof java.util.Date){
            TimeFormatEnum timeFormatEnum=timeFormatEnumMap.get(key);
            return DateUtils.dateFormat((Date)src,timeFormatEnum.getOutputFormatPattern());
        }
        return src.toString();
    }


}
