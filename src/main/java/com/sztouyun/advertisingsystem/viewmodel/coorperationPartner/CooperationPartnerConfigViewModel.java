package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CooperationPartnerConfigViewModel {

    @ApiModelProperty(value = "合作方id")
    private String id;

    @ApiModelProperty(value = "合作方编号")
    private String code;

    @ApiModelProperty(value = "合作方名称")
    private String name;

    @ApiModelProperty(value = "合作模式")
    private Integer cooperationPattern;

    @ApiModelProperty(value = "合作模式名称")
    private String cooperationPatternName;

    @ApiModelProperty(value = "是否禁用")
    private boolean disabled;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

}
