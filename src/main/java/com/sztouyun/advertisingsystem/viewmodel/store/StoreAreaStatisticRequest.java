package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/4/24.
 */
@ApiModel
@Data
public class StoreAreaStatisticRequest {
    @ApiModelProperty(value = "城市ID")
    private String cityId;
    @ApiModelProperty(value = "是否可用")
    private Boolean available;
    @ApiModelProperty(value = "门店类型")
    @EnumValue(enumClass = StoreTypeEnum.class,message = "门店类型不匹配",nullable = true)
    private Integer storeType;
}
