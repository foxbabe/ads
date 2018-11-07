package com.sztouyun.advertisingsystem.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.config.PreviewConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wenfeng on 2017/8/3.
 */
public class PdfUtils {
    private static final Logger logger = LoggerFactory.getLogger(PdfUtils.class);

    /**
     * 从PDF模板复制内容，填充表单写临时文件
     * @param previewConfig
     * @param content
     * @throws IOException
     * @throws DocumentException
     * @throws JSONException
     */
    public static void createPdfFromTemplate(PreviewConfig previewConfig, JSONObject content, ContractTemplate contractTemplate) throws IOException, DocumentException, JSONException {
        String destPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getTempFilePath(content.getString("contractId"));
        String templateName=content.get("contractTemplateId").toString()+Constant.TEMPLATE_SUFFIX;
//        if(previewConfig.getPkgtype().equals(Constant.WAR)){
//            String templatePath= FileUtils.getAssertsBasePath(previewConfig.getPkgtype())+previewConfig.getTempaltepath()+templateName;
//            reader=new PdfReader(templatePath);
//        }else{
//            InputStream stream = new FileUtils().getClass().getClassLoader().getResourceAsStream(previewConfig.getTempaltepath()+templateName);
//            reader=new PdfReader(stream);
//        }
        PdfReader reader =getPdfReader(previewConfig,templateName,contractTemplate);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BaseFont baseFont=getBaseFont(previewConfig);
        ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
        fontList.add(baseFont);
        PdfStamper ps = new PdfStamper(reader, bos);
        AcroFields acroFields = ps.getAcroFields();
        for (Map.Entry<String, Object> entry : content.entrySet()) {
            acroFields.setSubstitutionFonts(fontList);
            String key = new String(entry.getKey());
            String value=entry.getValue().toString();
            acroFields.setFieldProperty(key,"textsize",value.length(),null);
            acroFields.setFieldProperty(key,"textfont",baseFont,null);
            acroFields.regenerateField(key);
            acroFields.setField(key,value);
        }
        ps.setFormFlattening(true);
        ps.close();
        File fileResult=new File(destPath);
        FileOutputStream fos = new FileOutputStream(fileResult,true);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        reader.close();
    }

    /**
     * 从临时文件复制内容，追加表格内容，写到最终文件
     * @param previewConfig
     * @param content
     * @throws IOException
     * @throws DocumentException
     */
    public  static void  mergeTableToPdf(PreviewConfig previewConfig, JSONObject content) throws IOException, DocumentException {
        String id=content.getString("contractId");
        String destPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getDestFilePath(id);
        String srcPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getTempFilePath(id);
        Document document = new Document();
        PdfWriter writer  = PdfWriter.getInstance(document, new FileOutputStream(destPath));
        document.open();
        PdfReader reader = new PdfReader(srcPath);
        copyAllPages(reader,document,writer);
        document.newPage();
        JSONArray array=content.getJSONArray("storeList");
        if(!array.isEmpty()){
           writeTable(document,previewConfig,content);
        }
        document.close();
        writer.close();
        reader.close();
    }

    /**
     * 从PdfReader复制所有的page到Document
     * @param reader
     * @param document
     * @param writer
     */
    private static  void copyAllPages(PdfReader reader,Document document,PdfWriter writer){
        int n = reader.getNumberOfPages();
        PdfImportedPage page;
        for (int i = 1; i <= n; i++) {
            try {
                page = writer.getImportedPage(reader, i);
                Image image = Image.getInstance(page);
                image.setAbsolutePosition(0,0);
                document.add(image);
                document.newPage();
            } catch (BadElementException e) {
                e.printStackTrace();
                logger.error("document添加元素异常");
                throw new BusinessException("预览失败");
            } catch (DocumentException e) {
                e.printStackTrace();
                logger.error("Document操作异常");
                throw new BusinessException("预览失败");
            }

        }
    }

    /**
     * 向Document中追加表格
     * @param document
     * @param previewConfig
     * @param content
     * @throws DocumentException
     */
    private static void  writeTable(Document document,PreviewConfig previewConfig,JSONObject content) throws DocumentException {
        String []key=previewConfig.getKey();
        BaseFont baseFont= getBaseFont(previewConfig);
        Font font = new Font(baseFont);
        String indent="        ";
        for(int keyIndex=0;keyIndex<key.length;keyIndex++){
            String keyname=key[keyIndex];
            String wide=previewConfig.getWidth().get(keyname);
            float[] wideArray=previewConfig.stringToArray(wide);
            PdfPTable table = new PdfPTable(wideArray);// 5列的表格以及单元格的宽度。
            table.setTotalWidth(previewConfig.getWide());//表格整体宽度
            String []jsonKeys=previewConfig.getKeys().get(keyname).split(",");
            PdfPCell cell;

            Paragraph title = new Paragraph(indent+FileUtils.toUTF8(previewConfig.getTitle().get(keyname)),font);
            title.setAlignment(Element.ALIGN_CENTER);
            Phrase phrase=new Phrase(title);
            document.add(phrase);
            cell = new PdfPCell();
            cell.setColspan(wideArray.length);//占据5列
            String storeHeaders[]=FileUtils.toUTF8(previewConfig.getHeader().get(keyname)).split(",");
            for(int i=0;i<storeHeaders.length;i++){
                table.addCell(new Paragraph(storeHeaders[i],font));
            }
            if(keyname.equals("store")){
                JSONArray array=content.getJSONArray("storeList");
                for(int i=0;i<array.size();i++){
                    JSONObject jsonObject=array.getJSONObject(i);
                    table.addCell(new Paragraph(String.valueOf(i+1),font));
                    for(int j=0;j<jsonKeys.length;j++){
                        table.addCell(new Paragraph(jsonObject.getString(jsonKeys[j]),font));
                    }
                }
            }
            if(keyname.equals("playinfo")){
                JSONArray playInfos=content.getJSONArray("playinfos");
                for(int i=0;i<playInfos.size();i++){
                    JSONObject jsonObject=playInfos.getJSONObject(i);
                    for(int j=0;j<jsonKeys.length;j++){
                        table.addCell(new Paragraph(jsonObject.getString(jsonKeys[j]),font));
                    }
                }
            }
            document.add(table);
            document.newPage();
        }

    }

    private static  BaseFont getBaseFont(PreviewConfig previewConfig){
        BaseFont baseFont= null;//此方法不用导入iTextAsian.jar
        try {
            //baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            baseFont = BaseFont.createFont(getFontPath(previewConfig), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return baseFont;
    }

    private static  String getFontPath(PreviewConfig previewConfig){
        String fontFullPath="";
        if(previewConfig.getPkgtype().equals(Constant.JAR)){
            fontFullPath=previewConfig.getFullFontPath();
        }else{
            fontFullPath=FileUtils.getAssertsBasePath(previewConfig.getPkgtype())+previewConfig.getFontpath();
        }
        return  fontFullPath;
    }

    private  static PdfReader getPdfReader(PreviewConfig previewConfig,String templateName,ContractTemplate contractTemplate) throws IOException {
        String templatePath=null;
        PdfReader reader=null;
        if(previewConfig.getPkgtype().equals(Constant.JAR)){
            templatePath= FileUtils.getJarPath()+previewConfig.getTempaltepath()+templateName;
//            downloadTemplate(contractTemplate, templatePath);
//            InputStream stream = new FileUtils().getClass().getClassLoader().getResourceAsStream(templateName);
//            reader=new PdfReader(stream);
        }else{
            templatePath= FileUtils.getAssertsBasePath(previewConfig.getPkgtype())+templateName;
        }
        //FileUtils.downloadTemplate(contractTemplate,templatePath);
        reader=new PdfReader(templatePath);
        return reader;
    }


}
