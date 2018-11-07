package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StorePortraitEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel
public class StorePortraitChartStatisticRequest {

    @ApiModelProperty(value = "省份ID")
    @Size(max = 128,message ="省份id太长" )
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    @Size(max = 128,message ="城市id太长" )
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    @Size(max = 128,message ="地区id太长" )
    private String regionId;

    @ApiModelProperty(value = "门店画像的类型")
    @EnumValue(enumClass = StorePortraitEnum.class,message = "门店画像类型错误")
    private Integer storePortraitType;

}
