package com.sztouyun.advertisingsystem.viewmodel.partner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class DisplayTimesProportionViewModel {
    @ApiModelProperty("有效次数")
    private Long validTimes;

    @ApiModelProperty("有效占比")
    private String validProportion;

    @ApiModelProperty("无效次数")
    private Long invalidTimes;

    @ApiModelProperty("无效占比")
    private String invalidProportion;

    @ApiModelProperty("所有广告位占比情况")
    private List<DisplayTimesProportionItem> item;
}
