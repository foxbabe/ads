package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wenfeng on 2018/1/31.
 */
@ApiModel
@Data
public class MaterialStatusStatisticViewModel {
    @ApiModelProperty(value = "素材状态")
    private Integer materialStatus;

    @ApiModelProperty(value = "统计数量")
    private Long materialCount;

    public MaterialStatusStatisticViewModel(){}
    public MaterialStatusStatisticViewModel(Integer materialStatus, Long materialCount) {
        this.materialStatus = materialStatus;
        this.materialCount = materialCount;
    }
}
