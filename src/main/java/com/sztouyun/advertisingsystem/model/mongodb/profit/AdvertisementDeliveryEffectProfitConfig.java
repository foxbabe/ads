package com.sztouyun.advertisingsystem.model.mongodb.profit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

/**
 * 广告投放效果计费分成配置
 */
@ApiModel
@Data
public class AdvertisementDeliveryEffectProfitConfig {
    @Min(value = 1, message = "数量只能输入大于0的整数")
    @ApiModelProperty("数量")
    private Integer quantity;

    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "分成单价只能保留两位小数")
    @Min(value = 0, message = "分成单价只能输入大于等于0的数字")
    @ApiModelProperty("分成单价 单位: 元/天")
    private Double dailyAmount;
}
