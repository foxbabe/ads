package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class CooperationPartnerLineChartProfitTrendViewModel {

    @ApiModelProperty(value = "每天的收益金额")
    private List<CooperationPartnerLineChartProfitTrendInfo> cooperationPartnerLineChartProfitTrendInfos = new ArrayList<>();

    @ApiModelProperty("有效次数")
    private long validDisplayTimes;

    @ApiModelProperty("收益金额")
    private long profitAmount;

    public String getProfitAmount() {
        return NumberFormatUtil.format(profitAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    @JsonIgnore
    public long getProfitAmountLong() {
        return profitAmount;
    }
}
