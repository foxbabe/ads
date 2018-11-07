package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ClickOrScanTimesViewModel {
    @ApiModelProperty("广告位位置种类")
    private Integer advertisementPositionCategory;

    @ApiModelProperty("广告位名称")
    private String advertisementPositionName;

    @ApiModelProperty("点击次数")
    private Long clickTimes;

    @ApiModelProperty("扫码次数")
    private Long scanTimes;

    @ApiModelProperty("广告位占比")
    private String advertisementPositionProportion;

    @ApiModelProperty("点击次数占比")
    private String clickTimesProportion;

    @ApiModelProperty("扫码次数占比")
    private String scanTimesProportion;
}
