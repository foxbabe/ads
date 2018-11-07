package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;

@Data
@ApiModel
public class CityProfitRankingViewModel {
    @JsonIgnore
    @ApiModelProperty(value = "城市ID", hidden = true)
    private String cityId;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "收益金额")
    private double profit;

    public String getProfit() {
        return NumberFormatUtil.format(profit, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
