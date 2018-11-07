package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreProfitStatisticWithMonthViewModel {
    @ApiModelProperty(value = "系统分成总额")
    private Double totalShareAmount = 0D;
    @ApiModelProperty(value = "已结算总额")
    private Double settledAmount = 0D;
    @ApiModelProperty(value = "未结算总额")
    private Double unsettledAmount = 0D;

    public Double getUnsettledAmount() {
        return this.totalShareAmount - this.settledAmount;
    }
}
