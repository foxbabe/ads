package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DisplayTimesViewModel {
    @ApiModelProperty("合作方名称")
    private String partnerName;

    @ApiModelProperty("展示次数")
    private Long displayTimes;

    @ApiModelProperty("有效次数")
    private Long validTimes;

    @ApiModelProperty("有效占比")
    private String validProportion;

    @ApiModelProperty("无效次数")
    private Long invalidTimes;

    @ApiModelProperty("无效占比")
    private String invalidProportion;

}
