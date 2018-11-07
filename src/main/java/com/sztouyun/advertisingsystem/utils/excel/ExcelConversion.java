package com.sztouyun.advertisingsystem.utils.excel;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.StoreInvalidTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by szty on 2018/5/22.
 */
public class ExcelConversion {
    private static final Logger logger = LoggerFactory.getLogger(ExcelConversion.class);
    private static DecimalFormat df = new DecimalFormat("0");
    public static Integer transfer(MultipartFile file,ExcelConvertConfig config){
        Integer rowCount=0;
        BufferedInputStream inputStream=null;
        Workbook wb = null;
        BufferedWriter writer=null;
        File saveCSV = new File(config.getSavePath());
        try
        {
            if(saveCSV.exists()){
                saveCSV.delete();
            }
            saveCSV.createNewFile();
             writer = new BufferedWriter(new FileWriter(saveCSV));
            inputStream=new BufferedInputStream(file.getInputStream());
            wb=createWookbook(inputStream);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
            closeInputStream(inputStream);
            return rowCount;
        }

        Sheet sheet = wb.getSheetAt(0);
        if(sheet==null || config.getStartRowIndex()>sheet.getLastRowNum()){
            closeInputStream(inputStream);
            return 0;
        }
        StringBuffer content=new StringBuffer();
        Short firstColIndex=null;
        Short lastColIndex=null;
        String[] headerNames=config.getHeaderNames();
        if(headerNames.length>0){
            validateHeader(inputStream,sheet.getRow(0),headerNames);
        }
        for (int i = config.getStartRowIndex(); i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            if(row==null)
                continue;
            if(firstColIndex==null){
                firstColIndex=row.getFirstCellNum();
                lastColIndex=row.getLastCellNum();
            }
            if(headerNames.length>0 && lastColIndex>headerNames.length){
                lastColIndex=(short)headerNames.length;
            }
            if(!readRow(config, content, firstColIndex, lastColIndex, row)){
                rowCount++;
            }
            try {
                if(i>0 && i%config.getBatchSize()==0){
                    writer.write(content.toString());
                    content.setLength(0);
                }

            }catch (IOException ioe){
                logger.error(ioe.getMessage());
            }
        }

        try
        {
            if(!StringUtils.isEmpty(content)){
                writer.write(content.toString());
                content.setLength(0);
            }
            writer.close();
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }
        closeInputStream(inputStream);
        return rowCount;
    }

    private static Boolean readRow(ExcelConvertConfig config, StringBuffer content, Short firstColIndex, Short lastColIndex, Row row) {
        Boolean empty=Boolean.FALSE;
        Cell cell=null;
        int filterColsLength=config.getFilterCols().length;
        Integer currentLength=content.length();
        if(filterColsLength>0){
            for(int j=0;j<filterColsLength;j++){
                int colIndex=config.getFilterCols()[j];
                if(colIndex<firstColIndex || colIndex>=lastColIndex){
                    content.append("");
                }else{
                    cell=row.getCell(colIndex);
                    if(cell!=null){
                        appendCellValue(cell,content);
                    }
                }
                if(j<filterColsLength-1){
                    content.append(config.getSeparator());
                }else{
                    content.append(config.getNewLine());
                }
            }
            empty= (content.length()-currentLength)==getEmptyCsvLineLength(config.getFilterCols().length);
        }else{
            for (int j=firstColIndex;j<lastColIndex;j++){
                cell=row.getCell(j);
                if(cell!=null){
                    appendCellValue(cell,content);
                }
                if(j<lastColIndex-1){
                    content.append(config.getSeparator());
                }else{
                    content.append(config.getNewLine());
                }
            }
            empty= (content.length()-currentLength)==getEmptyCsvLineLength(lastColIndex-firstColIndex);
        }
        if(empty){
            content.delete(currentLength,content.length());
        }
        return empty;
    }

    public static Workbook createWookbook(BufferedInputStream inputStream){
        try {
            String headerInfo= FileUtils.getFileHeader(inputStream,4);
            if(headerInfo.equals("d0cf11e0")){
                return new HSSFWorkbook(inputStream);
            }else if(headerInfo.equals("504b0304")){
                return new XSSFWorkbook(inputStream);
            }else
                throw new BusinessException("文件格式不支持");
        } catch (IOException e) {
            logger.error("解析Excel文件异常",e);
            throw new BusinessException("解析Excel文件异常");
        }
    }
    public static DBObject readRowToDbObject(ExcelConvertConfig config, Short firstCellIndex, Short lastCellIndex, Row row) {
        Map<String,Object> defaultValueMap=config.getDefaultValueMap();
        Cell cell=null;
        Boolean valid=false;
        int filterRowsLength=config.getFilterCols().length;
        DBObject object=new BasicDBObject();
        if(filterRowsLength>0){
            for(int j=0;j<filterRowsLength;j++){
                int colIndex=config.getFilterCols()[j];
                String key=config.getFilterCellKeys()[j];
                Object value="";
                if(colIndex<firstCellIndex || colIndex>=lastCellIndex)
                    throw new BusinessException("第"+(row.getRowNum()+1)+"行,["+config.getHeaderNames()[colIndex]+"]列不允许为空");
                cell=row.getCell(colIndex);
                if(cell!=null){
                    value=getOriginValue(key,cell,config.getCurrencyKeys());
                }
                if(emptyObject(value)){
                    value=defaultValueMap.get(key);
                }
                if( cell!=null && cell.getCellType()!=Cell.CELL_TYPE_BLANK){
                    valid=true;
                }
                object.put(key,value);
            }
        }else{
            for (int j=firstCellIndex;j<lastCellIndex;j++){
                cell=row.getCell(j);
                String key=config.getFilterCellKeys()[j];
                Object value="";
                if(cell!=null){
                    value=getOriginValue(key,cell,config.getCurrencyKeys());
                }
                if(emptyObject(value)){
                    value=defaultValueMap.get(key);
                }
                if(cell!=null && cell.getCellType()!=Cell.CELL_TYPE_BLANK){
                    valid=true;
                }
                object.put(key,value);
            }
        }
        if(!valid)
            return null;
        object.put("oid",config.getOid());
        object.put("bid",config.getBid());
        object.put("validType", 0);
        return object;
    }

    private static void appendCellValue(Cell cell,StringBuffer content){
        int cellType=cell.getCellType();
        switch (cellType){
            case Cell.CELL_TYPE_BOOLEAN:
                content.append(cell.getBooleanCellValue()?1:0);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                content.append(df.format(cell.getNumericCellValue()));
                break ;
            case Cell.CELL_TYPE_STRING:
                 content.append(filerStringValue(cell.getStringCellValue().replaceAll("\r|\n", "")));
                 break;
            default:
        }
    }

    private static String getOriginStringValue(Cell cell){
        int cellType=cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue() ? 1 : 0);
            case Cell.CELL_TYPE_NUMERIC:
                return df.format(cell.getNumericCellValue());
            default:
                return cell.getStringCellValue().replaceAll("\r|\n", "");
        }
    }

    public static Object filerStringValue(String src){
        //todo 日期处理
        if(Arrays.asList(Constant.TRUE_BOOLEAN.split(Constant.SEPARATOR)).contains(src))
            return 1;
        if(Arrays.asList(Constant.FALSE_BOOLEAN.split(Constant.SEPARATOR)).contains(src))
            return 0;
        return src;
    }

    public static void validateHeader(BufferedInputStream inputStream,Row header,String[] headerNames){
        if(header==null || header.getLastCellNum()<headerNames.length){
            closeInputStream(inputStream);
            throw new BusinessException("模板格式不正确");
        }
        Cell headerCell=null;
        for (int i=0;i<headerNames.length;i++){
            headerCell=header.getCell(i);
            if( !getOriginStringValue(headerCell).equals(headerNames[i])) {
                closeInputStream(inputStream);
                throw new BusinessException("模板格式不正确");
            }
        }
    }

    public static void closeInputStream(BufferedInputStream inputStream){
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object getOriginValue(Cell cell){
        return getOriginValue(null,cell,null);
    }
    private static Object getOriginValue(String key,Cell cell, List<String> currencyKeys){
        try {
            int cellType=cell.getCellType();
            if(!CollectionUtils.isEmpty(currencyKeys) && currencyKeys.contains(key)) {
                return Long.valueOf(df.format(cell.getNumericCellValue() * 100));
            }
            switch (cellType) {
                case Cell.CELL_TYPE_BOOLEAN:
                    return cell.getBooleanCellValue();
                case Cell.CELL_TYPE_NUMERIC:
                    return df.format(cell.getNumericCellValue());
                default:
                    return cell.getStringCellValue().replaceAll("\r|\n", "");
            }
        }catch (Exception e){
            throw new BusinessException("第"+(cell.getRowIndex()+1)+"行,第"+(cell.getColumnIndex()+1)+"列数据格式不正确");
        }

    }

    private static int getEmptyCsvLineLength(Integer fieldCount){
        return fieldCount;
    }

    private static boolean emptyObject(Object o){
        return o==null || StringUtils.isEmpty(o.toString())?true:false;
    }
}
