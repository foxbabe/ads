package com.sztouyun.advertisingsystem.viewmodel.advertisement.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AdvertisementMaterialStatisticsViewModel {

    @ApiModelProperty("素材类型 1:图片 2:文本 3:视频")
    private Integer materialType;

    @ApiModelProperty("对应素材总数")
    private Long totalMaterials;

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public Long getTotalMaterials() {
        return totalMaterials;
    }

    public void setTotalMaterials(Long totalMaterials) {
        this.totalMaterials = totalMaterials;
    }
}

