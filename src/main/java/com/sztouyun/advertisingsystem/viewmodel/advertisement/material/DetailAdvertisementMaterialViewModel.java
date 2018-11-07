package com.sztouyun.advertisingsystem.viewmodel.advertisement.material;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class DetailAdvertisementMaterialViewModel {

    @ApiModelProperty(value = "素材类型: 1:图片 2:文本 3:视频")
    private Integer materialType;

    @ApiModelProperty(value = "当素材类型为2时候, 为文本内容; 否则, 为视频或者图片的url")
    private String data;

    @ApiModelProperty(value = "素材id")
    private String id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "广告尺寸类型")
    private Integer positionType;

    @ApiModelProperty(value = "广告尺寸名称")
    private String positionName;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
