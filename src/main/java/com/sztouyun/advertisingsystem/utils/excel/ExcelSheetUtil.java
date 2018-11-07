package com.sztouyun.advertisingsystem.utils.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by fengwen on 15/11/2017.
 */
public class ExcelSheetUtil {
    public static void addSheet(Workbook workbook,ExcelData excelData){
        Sheet sheet = workbook.createSheet(excelData.getSheetName());
        setSheetHeader(workbook,sheet, excelData);
        setSheetData(excelData, sheet);

    }

    private static  void setSheetData(ExcelData excelData, Sheet sheet) {
        Map<String,TimeFormatEnum > timeFormatEnumMap=excelData.getTimeFormatMap();
        Map<String,BooleanTypeEnum> booleanTypeEnumMap=excelData.getBooleanTypeMap();
        int startRow=excelData.getHeaders().size();
        //table对应的KEY在最下级的Header
        ExcelHeader mainTableHeader=excelData.getHeaders().get(startRow-1);
        String[] keys=mainTableHeader.getHeaderKeys();
        JSONArray data=excelData.getJsonArray();
        if(data==null)
            return ;
        Row row;
        for(int i=0,line=data.size();i<line;i++){
            row = sheet.createRow(i + startRow);
            JSONObject jsonObject=data.getJSONObject(i);
            if(mainTableHeader.getHasSortNumber()){
                row.createCell(0).setCellValue(i+1);  //序号列
                for(int j=0,cel=keys.length;j<cel;j++){
                    row.createCell(j+1).setCellValue(filterValue(keys[j],jsonObject.get(keys[j]),timeFormatEnumMap,booleanTypeEnumMap));
                }
            }else{
                for(int j=0,cel=keys.length;j<cel;j++){
                    row.createCell(j).setCellValue(filterValue(keys[j],jsonObject.get(keys[j]),timeFormatEnumMap,booleanTypeEnumMap));
                }
            }

        }
    }

    private static void setSheetHeader(Workbook workbook, Sheet sheet, ExcelData excelData) {
        sheet.setDefaultColumnWidth((short)15);
        List<ExcelHeader> headers=excelData.getHeaders();
        for(int i=0,len=headers.size();i<len;i++){
            ExcelHeader header=headers.get(i);
            CellRangeAddress cellRangeAddress=header.getCellRangeAddress();
            if(cellRangeAddress!=null){
                sheet.addMergedRegion(cellRangeAddress);
            }
            Row row = sheet.createRow(i);
            String[] headerNames=header.getHeaderNames();
            for(int j=0,names=headerNames.length;j<names;j++){
                Cell cell = row.createCell((short) j);
                cell.setCellValue(headerNames[j]);
                cell.setCellStyle(getCellStyle(workbook,header.getAlignType()));

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

    private static String filterValue(String key,Object src,Map<String,TimeFormatEnum> timeFormatEnumMap,Map<String,BooleanTypeEnum> booleanTypeEnumMap){
        if(src==null)
            return "";
        if(src instanceof java.lang.Boolean){
            return (booleanTypeEnumMap==null || !booleanTypeEnumMap.containsKey(key))?BooleanTypeEnum.Default.getBooleanName((boolean)src):(booleanTypeEnumMap.get(key).getBooleanName((boolean)src));
        }else if(src instanceof java.util.Date){
            TimeFormatEnum timeFormatEnum=timeFormatEnumMap.get(key);
            if(timeFormatEnum==null){
                timeFormatEnum=TimeFormatEnum.Default;
            }
            return DateUtils.dateFormat((Date)src,timeFormatEnum.getOutputFormatPattern());
        }
        return src.toString();
    }
}
