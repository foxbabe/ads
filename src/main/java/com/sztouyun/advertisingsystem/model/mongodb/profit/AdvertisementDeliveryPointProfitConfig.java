package com.sztouyun.advertisingsystem.model.mongodb.profit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 广告投放点计费分成配置
 */
@ApiModel
@Data
public class AdvertisementDeliveryPointProfitConfig {
    @Max(value = 24, message = "开机时长不能大于24小时")
    @Min(value = 1, message = "开机时长不能小于1小时")
    @NotNull(message = "开机时长不能为空")
    @ApiModelProperty("开机时长 单位: 小时/天")
    private Integer bootHour;

    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "分成单价只能保留两位小数")
    @Min(value = 0, message = "分成单价只能输入大于等于0的数字")
    @NotNull(message = "分成单价不能为空")
    @ApiModelProperty("分成单价 单位: 元/天")
    private Double dailyAmount;
}
