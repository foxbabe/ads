package com.sztouyun.advertisingsystem.viewmodel.advertisement.material;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class AdvertisementMaterialPageInfoViewModel extends BasePageInfo {

    @ApiModelProperty(value = "客户id", required = true)
    @NotNull(message = "客户id不能为空")
    @Size(max = Constant.INTEGER_MAX, message = "客户id长度不能超过"+Constant.INTEGER_MAX)
    private String customerId;

    @ApiModelProperty(value = "素材类型: 1:图片 2:文本 3:视频", required = true)
    @NotNull(message = "素材类型不能为空")
    @EnumValue(enumClass = MaterialTypeEnum.class, message = "素材类型不正确")
    private Integer materialType;

    @ApiModelProperty(value = "尺寸位置ID")
    private String positionId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
