package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class BaseAdvertisementSizeConfigViewModel {

    @ApiModelProperty(value = "尺寸名称", required = true)
    private String sizeName;

    @ApiModelProperty(value = "水平分辨率", required = true)
    @Min(value = 1, message = "最小值不能小于1")
    @Max(value= Constant.INTEGER_MAX,message = "制作费用最大值不能超过999999999")
    @NotNull(message = "请输入有效的分辨率")
    private Integer horizontalResolution;

    @ApiModelProperty(value = "垂直分辨率", required = true)
    @Min(value = 1, message = "最小值不能小于1")
    @Max(value= Constant.INTEGER_MAX,message = "制作费用最大值不能超过999999999")
    @NotNull(message = "请输入有效的分辨率")
    private Integer verticalResolution;

    @ApiModelProperty(value = "宽度比", required = true)
    @Min(value = 1, message = "最小值不能小于1")
    @Max(value= Constant.INTEGER_MAX,message = "制作费用最大值不能超过999999999")
    @NotNull(message = "请输入有效的宽高比")
    private Integer widthRatio;

    @ApiModelProperty(value = "高度比", required = true)
    @Min(value = 1, message = "最小值不能小于1")
    @Max(value= Constant.INTEGER_MAX,message = "制作费用最大值不能超过999999999")
    @NotNull(message = "请输入有效的宽高比")
    private Integer highRatio;
    @ApiModelProperty(value = "终端类型", required = true)
    @EnumValue(enumClass = TerminalTypeEnum.class,nullable = false,message = "终端类型不匹配")
    private Integer terminalType;
    @ApiModelProperty(value = "广告位类型", required = true)
    @EnumValue(enumClass = AdvertisementPositionTypeEnum.class,nullable = false,message = "广告位类型不匹配")
    private Integer advertisementPositionType;

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getAdvertisementPositionType() {
        return advertisementPositionType;
    }

    public void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    public void setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    public void setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public Integer getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(Integer widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Integer getHighRatio() {
        return highRatio;
    }

    public void setHighRatio(Integer highRatio) {
        this.highRatio = highRatio;
    }
}
