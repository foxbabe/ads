package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreInfoStatisticViewModel extends BaseStoreInfoViewModel {
    @ApiModelProperty(value="门店ID")
    private String storeId;

    @ApiModelProperty(hidden = true)
    private Long displayTimes;

    @ApiModelProperty(value="是否铺货")
    private Boolean isPaveGoods;

    @ApiModelProperty(value="有无门店画像")
    private Boolean isStorePortrait;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;
}
