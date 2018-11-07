package com.sztouyun.advertisingsystem.viewmodel.monitor;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PartnerAdvertisementMonitorCountStatisticInfo {

    @ApiModelProperty(value = "全部第三方广告数量")
    private Long totalCount;

    @ApiModelProperty(value = "监控中")
    private Long OnWatchingCount;

    @ApiModelProperty(value = "已完成")
    private Long finishedCount;

    public Long getFinishedCount() {
        return totalCount-OnWatchingCount;
    }
}
