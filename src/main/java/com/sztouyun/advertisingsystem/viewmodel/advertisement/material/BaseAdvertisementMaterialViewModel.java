package com.sztouyun.advertisingsystem.viewmodel.advertisement.material;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class BaseAdvertisementMaterialViewModel {

    @ApiModelProperty(value = "素材类型: 1:图片 2:文本 3:视频", required = true)
    @NotNull(message = "素材类型不能为空")
    @Max(value = Constant.INTEGER_MAX, message = "请输入有效的素材类型")
    private Integer materialType;

    @ApiModelProperty(value = "文本内容, 当类型为文本的时候, 该项必填, 其他为非必填")
    @Size(max = 255)
    private String content;

    @ApiModelProperty(value = "尺寸类型: 0:默认，1:全屏 2:四分之三", required = false)
    private Integer positionType=0;

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }
}
