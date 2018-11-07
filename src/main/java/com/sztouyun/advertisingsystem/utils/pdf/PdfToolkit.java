package com.sztouyun.advertisingsystem.utils.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.utils.FreeMarkerUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by szty on 2018/6/26.
 */
@Data
public class PdfToolkit {
    private static final Logger logger = LoggerFactory.getLogger(PdfToolkit.class);

    private HeaderFooterBuilder headerFooterBuilder;
    private String saveFilePath;
    private String templatePath;

    /**
     * @description     导出pdf到文件
     * @param templatePath  输出PDF文件名
     * @param data      模板所需要的数据
     *
     */
    public String exportToFile(String templatePath,Object data){

        String htmlData= FreeMarkerUtil.getFreeMakerTemplate(templatePath,data);
        if(StringUtils.isEmpty(saveFilePath)){
            saveFilePath=getDefaultSavePath(templatePath);
        }
        File file=new File(saveFilePath);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream=null;
        try{
            //设置输出路径
            outputStream=new FileOutputStream(saveFilePath);
            //设置文档大小
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            //设置页眉页脚
            PDFBuilder builder = new PDFBuilder(headerFooterBuilder,data);
            builder.setPresentFontSize(10);
            writer.setPageEvent(builder);

            //输出为PDF文件
            convertToPDF(writer,document,htmlData);
        }catch(Exception ex){
            logger.error("PDF export to File fail",ex);
            throw new BusinessException("PDF export to File fail");
        }finally{
            FileUtils.closeOutputStream(outputStream);
        }
        return saveFilePath;

    }

    /**
     * @description PDF文件生成
     */
    private  void convertToPDF(PdfWriter writer,Document document,String htmlString){
        //获取字体路径
        String fontPath=PdfResourceTypeEnum.Font.getResourcePathDir();
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer,document,
                    new ByteArrayInputStream(htmlString.getBytes()),
                    XMLWorkerHelper.class.getResourceAsStream("/default.css"),
                    Charset.forName("UTF-8"),new XMLWorkerFontProvider(fontPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("PDF文件生成异常");
        }finally {
            document.close();
        }

    }
    /**
     * @description 创建默认保存路径
     */
    private  String  getDefaultSavePath(String fileName){
        String classpath=PdfToolkit.class.getClassLoader().getResource("").getPath();
        String saveFilePath=classpath+"pdf/"+fileName;
        File f=new File(saveFilePath);
        if(!f.getParentFile().exists()){
            f.mkdirs();
        }
        return saveFilePath;
    }


    public HeaderFooterBuilder getHeaderFooterBuilder() {
        return headerFooterBuilder;
    }

    public void setHeaderFooterBuilder(HeaderFooterBuilder headerFooterBuilder) {
        this.headerFooterBuilder = headerFooterBuilder;
    }
    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }



}
