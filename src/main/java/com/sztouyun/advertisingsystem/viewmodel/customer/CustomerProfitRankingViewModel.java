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
public class CustomerProfitRankingViewModel {
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "收益金额")
    private double profit;

    @JsonIgnore
    @ApiModelProperty(value = "总收益", hidden = true)
    private double totalProfit;

    @ApiModelProperty("收益占比")
    public String getProfitProportion() {
        return NumberFormatUtil.format(profit, totalProfit, Constant.RATIO_PATTERN);
    }

    public String getProfit() {
        return NumberFormatUtil.format(profit, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
