package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreNetworkProportionViewModel {
    @ApiModelProperty("有网络（开机）门店数量")
    private Long onStoreTotal;

    @ApiModelProperty("无网络（关机）门店数量")
    private Long offStoreTotal;

    @ApiModelProperty("有网络（开机）门店占比")
    private String onStoreProportion;

    @ApiModelProperty("无网络（关机）门店占比")
    private String offStoreProportion;
}
