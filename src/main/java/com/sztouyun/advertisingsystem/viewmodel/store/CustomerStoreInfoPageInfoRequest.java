package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomerStoreInfoPageInfoRequest extends BaseStoreInfoPageInfoViewModel {
    @ApiModelProperty(value = "选点记录ID")
    private String customerStorePlanId;

    @ApiModelProperty(value = "是否勾选")
    private Boolean isCheck;
}
