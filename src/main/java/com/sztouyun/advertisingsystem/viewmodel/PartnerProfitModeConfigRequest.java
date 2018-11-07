package com.sztouyun.advertisingsystem.viewmodel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by szty on 2018/9/11.
 */
@ApiModel
public class PartnerProfitModeConfigRequest {
    @ApiModelProperty(value = "合作方ID")
    private String id;
    @ApiModelProperty(value = "配置组")
    private Integer configGroup;
}
