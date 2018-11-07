package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class
StoreNetworkPeriodIntervalChartInfo {

    @ApiModelProperty(value = "时段区间")
    private Integer interval;
    @ApiModelProperty(value = "4G门店数量")
    private Integer storeCount4G;
    @ApiModelProperty(value = "WIFI门店数量")
    private Integer storeCountWIFI;
    @ApiModelProperty(value = "有网络门店数量")
    private Integer networkStoreCount;
}
