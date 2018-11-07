package com.sztouyun.advertisingsystem.viewmodel.partner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DisplayTimesProportionItem {
    @ApiModelProperty("广告位名称")
    private String advertisementPositionCategoryName;

    @ApiModelProperty("有效次数")
    private Long validTimes;

    @ApiModelProperty("有效占比")
    private String validProportion;

    @ApiModelProperty("无效次数")
    private Long invalidTimes;

    @ApiModelProperty("无效占比")
    private String invalidProportion;
}
