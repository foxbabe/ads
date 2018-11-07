package com.sztouyun.advertisingsystem.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Created by wenfeng on 2017/11/8.
 */
public class ExcelUtil {
    /**
     *
     * @param wb
     * @param styleType  对齐方式
     * @param backColor  背景色
     * @return
     */
    public static HSSFCellStyle getCellStyle(HSSFWorkbook wb, Short styleType, Short backColor){
        HSSFFont font=wb.createFont();
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short)11);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(styleType);
        if(backColor!=0){
//            style.setFillForegroundColor(backColor);
//            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        style.setFont(font);
        return  style;
    }
}
