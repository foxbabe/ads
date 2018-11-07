package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
@Data
public class CustomerStoreInfoAreaSelectedRequest extends StoreInfoAreaSelectedRequest{
    @ApiModelProperty(value = "客户选点记录ID")
    private String customerStorePlanId;
}
