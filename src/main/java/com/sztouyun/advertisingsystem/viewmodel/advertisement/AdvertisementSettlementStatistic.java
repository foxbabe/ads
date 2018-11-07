package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AdvertisementSettlementStatistic {

    @ApiModelProperty(value = "门店ID")
    private String storeId;

    @ApiModelProperty(value = "分成总额")
    private Long totalShareAmount;

    @ApiModelProperty(value = "已结算总额")
    private Long settledAmount;
}
