package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdvertisementTerminalTypeDisplayTimesInfo {

    @ApiModelProperty(value = "终端类型, TerminalTypeEnum")
    private Integer terminalType;

    @ApiModelProperty(value = "显示次数")
    private Integer displayTimes;

    @ApiModelProperty(value = "广告id")
    private String advertisementId;
}
