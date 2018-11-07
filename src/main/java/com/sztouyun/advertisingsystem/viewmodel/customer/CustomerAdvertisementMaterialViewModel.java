package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class CustomerAdvertisementMaterialViewModel {

    @ApiModelProperty(value = "素材id")
    private String id;

    @ApiModelProperty(value = "上传时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "上传人名字")
    private String creatorName;

    @ApiModelProperty(value = "素材大小, 仅用在视频和图片")
    private String materialSize;

    @ApiModelProperty(value = "图片规格, 仅用在图片")
    private String materialSpecification;

    @ApiModelProperty(value = "素材类型名称")
    private String materialTypeName;

    @ApiModelProperty(value = "素材名称, 文本使用文本内容")
    private String materialName;

    @ApiModelProperty(value = "素材url, 只用在图片和视频, 如果用在图片上面, 可以作为图片的logo字段")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getMaterialSize() {
        return materialSize;
    }

    public void setMaterialSize(String materialSize) {
        this.materialSize = materialSize;
    }

    public String getMaterialSpecification() {
        return materialSpecification;
    }

    public void setMaterialSpecification(String materialSpecification) {
        this.materialSpecification = materialSpecification;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

}
