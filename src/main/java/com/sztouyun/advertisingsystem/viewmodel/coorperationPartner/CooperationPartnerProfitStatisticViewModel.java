package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;


import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;

@ApiModel
@Data
public class CooperationPartnerProfitStatisticViewModel {

    @ApiModelProperty(value = "总收益")
    private Long totalProfit;

    @ApiModelProperty(value = "昨日收益")
    private Long yesterdayProfit;

    @ApiModelProperty(value = "前7日收益")
    private Long lastWeekProfit;

    @ApiModelProperty(value = "本月收益")
    private Long monthProfit;

    public String getTotalProfit() {
        return NumberFormatUtil.format(totalProfit/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getYesterdayProfit() {
        return NumberFormatUtil.format(yesterdayProfit/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getLastWeekProfit() {
        return NumberFormatUtil.format(lastWeekProfit/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getMonthProfit() {
        return NumberFormatUtil.format(monthProfit/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
