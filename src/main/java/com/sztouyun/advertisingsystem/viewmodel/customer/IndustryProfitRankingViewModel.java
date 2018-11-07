package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.RoundingMode;

@Data
@NoArgsConstructor
public class IndustryProfitRankingViewModel {

    @ApiModelProperty(value = "行业ID")
    private String industryId;

    @ApiModelProperty(value = "行业名称")
    private String industryName;

    @ApiModelProperty(value = "收益金额")
    private double profit;

    @JsonIgnore
    @ApiModelProperty(value = "总收益", hidden = true)
    private double totalProfit;

    @ApiModelProperty(value = "收益占比")
    private String profitRatio;

    public String getProfit() {
        return NumberFormatUtil.format(profit, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getProfitRatio() {
        return NumberFormatUtil.format(profit,totalProfit, Constant.RATIO_PATTERN);
    }
}
