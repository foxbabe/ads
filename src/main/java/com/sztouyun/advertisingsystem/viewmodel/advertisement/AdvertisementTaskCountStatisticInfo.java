package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementTaskCountStatisticInfo {

    @ApiModelProperty(value = "全部广告任务数量")
    private Long totalCount;

    @ApiModelProperty(value = "异常")
    private Long abnormalCount;

    @ApiModelProperty(value = "非异常")
    private Long normalCount;

    public Long getNormalCount() {
        return totalCount-abnormalCount;
    }
}
