package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.material.MaterialContentTypeEnum;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wenfeng on 2018/1/30.
 */
@Data
public class AdsFile {
    private BufferedInputStream inputStream;
    private int width=0;
    private int height=0;
    private MaterialContentTypeEnum materialContentTypeEnum;
    private String magicNumber;
    private long length;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public AdsFile(){}
    public AdsFile(String url,Integer materialType){
        try {
            URLConnection urlConnection=new URL(url).openConnection();
            length=urlConnection.getContentLength();
            inputStream=new BufferedInputStream(urlConnection.getInputStream());
            magicNumber=FileUtils.getFileHeader(inputStream,10);
            if(materialType.equals(MaterialTypeEnum.Img.getValue())){
                inputStream.mark((int)length+1);
                BufferedImage sourceImg = ImageIO.read(inputStream);
                 width=sourceImg.getWidth();
                 height=sourceImg.getHeight();
                inputStream.reset();
            }
            this.materialContentTypeEnum=getMaterialContentTypeEnum(magicNumber,materialType);
        } catch (IOException e) {
            logger.error("读取网络文件失败");
            throw new BusinessException("读取网络文件失败");
        }
    }

    private MaterialContentTypeEnum getMaterialContentTypeEnum(String magicNumber,Integer materialType){
        MaterialContentTypeEnum materialContentTypeEnum=MaterialContentTypeEnum.getMaterialContentTypeByMagicNumber(magicNumber);
        if(!materialContentTypeEnum.getMaterialTypeEnum().getValue().equals(materialType))
            throw new BusinessException("所传素材类型与素材不匹配");
        return materialContentTypeEnum;
    }

    public void close(){
        try {
            this.inputStream.close();
        } catch (IOException e) {
            logger.error("处理素材文件异常");
            throw new BusinessException("释放资源异常");
        }
    }

    public String getImgSpecification() {
        return this.width + "*" + this.height;
    }




}
