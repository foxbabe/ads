package com.sztouyun.advertisingsystem.utils.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.sztouyun.advertisingsystem.common.Constant;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 页眉页脚定制工具
 */
public class PDFHeaderFooter implements HeaderFooterBuilder {
    /**
     * @param writer   PDF编写类
     * @param document PDF文档对象
     * @param data     业务数据
     * @param font     字体设置
     * @param template PDF模板
     * @description PDF页脚设置类
     */
    public void writeFooter(PdfWriter writer,
                            Document document,
                            Object data,
                            Font font,
                            PdfTemplate template) {
        if (data == null) {
            return;
        }
        int pageS = writer.getPageNumber();
        int currentPage = pageS;
        //System.out.println("currentPage:"+currentPage);
        if (currentPage < 0) {
            return;
        }
        Phrase footer2 = new Phrase( currentPage + " / ", font);

        PdfContentByte cb = writer.getDirectContent();
        ColumnText.showTextAligned(
                cb,
                Element.ALIGN_CENTER,
                footer2,
                (document.right()/2),
                document.bottom() - 20, 0);
        cb.addTemplate(template, document.right()/2+10, document.bottom() - 20);

    }

    /**
     * @param writer   PDF编写类
     * @param document PDF文档对象
     * @param data     业务数据
     * @param font     字体设置
     * @param template PDF模板
     * @description PDF页头设置类
     */
    public void writeHeader(PdfWriter writer,
                            Document document,
                            Object data,
                            Font font,
                            PdfTemplate template) {

        try {
            Image image = Image.getInstance(PdfResourceTypeEnum.Logo.getResourcePath());
            image.scalePercent(50f);
            image.setAlignment(Element.ALIGN_LEFT);
            Rectangle rectangle=new Rectangle(document.left()+40,document.top()-14,document.right()-40,document.top()-15);
            image.setAbsolutePosition(document.left()+40,document.top()-10);
            Paragraph line = new Paragraph();
            Chunk chunk=new Chunk(new LineSeparator());
            line.add(chunk);
            rectangle.setBackgroundColor(BaseColor.BLACK);
            document.add(image);
            document.add(rectangle);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_RIGHT,
                new Phrase(Constant.PDF_HEADER_COMPANY_NAME,font),
                document.right()-40,
                document.top()-10, 0);
    }


    /**
     * @param writer   PDF编写类
     * @param document PDF文档对象
     * @param data     业务数据
     * @description 页头、页眉设置的模板替换类
     */
    public String getReplaceOfTemplate(PdfWriter writer, Document document, Object data) {
        int total = writer.getPageNumber();
        return total + "";
    }

}