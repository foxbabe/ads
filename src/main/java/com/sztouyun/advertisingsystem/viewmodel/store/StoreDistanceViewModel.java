package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class StoreDistanceViewModel extends StoreInfoPageInfoViewModel {

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "客户选点ID")
    private String customerStorePlanId;
}
