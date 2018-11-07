package com.sztouyun.advertisingsystem.viewmodel.order;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class OrderMaterialViewModel {
    @ApiModelProperty(value = "展示次数", required = true)
    @NotNull(message = "展示次数不能为空")
    @Min(value = 0,message = "展示次数必须大于0")
    private Integer  displayTimes;

    @ApiModelProperty(value = "展示时长", required = true)
    @NotNull(message = "展示时长不能为空")
    @Min(value = 0,message = "展示时长必须大于0")
    private Integer duration;

    @ApiModelProperty(value = "广告位种类", required = true)
    @EnumValue(enumClass = AdvertisementPositionCategoryEnum.class,message = "广告位种类不匹配")
    private Integer advertisementPositionCategory;
}
