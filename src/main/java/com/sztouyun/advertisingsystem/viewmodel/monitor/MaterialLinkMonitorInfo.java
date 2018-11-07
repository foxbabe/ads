package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class MaterialLinkMonitorInfo {

    @ApiModelProperty(value = "广告素材链接步骤ID")
    private String materialUrlStepId;

    @ApiModelProperty(value = "次数")
    private Long triggerTimes;

    @ApiModelProperty(value = "步骤类型: AdvertisementMaterialUrlStepTypeEnum")
    private Integer stepType;

    @ApiModelProperty(value = "步骤对应的操作：MaterialLinkActionEnum")
    private Integer action;
}