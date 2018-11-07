package com.sztouyun.advertisingsystem.model.material;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;

/**
 * Created by wenfeng on 2018/2/2.
 */
public enum MaterialContentTypeEnum {
    JPEG("ffd8ffe000104a464946","image/jpeg",MaterialTypeEnum.Img,"jpeg"),
    PNG("89504e470d0a1a0a0000","image/png",MaterialTypeEnum.Img,"png"),
    BMP("424d","image/bmp",MaterialTypeEnum.Img,"bmp"),
    FLV("464c5601050000000900","video/x-flv",MaterialTypeEnum.Video,"flv"),
    AVI("52494646","video/avi",MaterialTypeEnum.Video,"avi"),
    MP4("667479706973","video/mp4",MaterialTypeEnum.Video,"mp4"),
    MP42("00000018667479706d70","video/mp4",MaterialTypeEnum.Video,"mp4"),
    WMV("3026b2758e66cf11a6d9","video/x-ms-wmv",MaterialTypeEnum.Video,"wmv"),
    MKV("1a45dfa","video/x-matroska",MaterialTypeEnum.Video,"mkv");
    private String magicNumber;
    private String contentType;
    private MaterialTypeEnum materialTypeEnum;
    private String suffix;


     MaterialContentTypeEnum(String magicNumber,String contentType,MaterialTypeEnum materialTypeEnum,String suffix){
        this.magicNumber=magicNumber;
        this.contentType=contentType;
        this.materialTypeEnum=materialTypeEnum;
        this.suffix=suffix;
    }

    public String getContentType() {
        return contentType;
    }

    public String getMagicNumber() {
        return magicNumber;
    }

    public MaterialTypeEnum getMaterialTypeEnum() {
        return materialTypeEnum;
    }

    public void setMaterialTypeEnum(MaterialTypeEnum materialTypeEnum) {
        this.materialTypeEnum = materialTypeEnum;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public static MaterialContentTypeEnum getMaterialContentTypeByMagicNumber(String magicNumber){
        for(MaterialContentTypeEnum typeEnum : MaterialContentTypeEnum.values()) {
            if(magicNumber.startsWith(typeEnum.magicNumber) || magicNumber.contains(typeEnum.magicNumber)) {
                return typeEnum;
            }
        }
        throw new BusinessException("不支持该类型");
    }
}
