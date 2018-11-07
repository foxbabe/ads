package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModelProperty;

public class AdvertisementStatusStatisticsViewModel {

    @ApiModelProperty("投放中广告数量")
    private Integer deliveringTotals;

    @ApiModelProperty("下架广告数量")
    private Integer takeOffTotals;

    @ApiModelProperty("高风险投放中广告数量")
    private Integer highRiskTotals;

    public Integer getDeliveringTotals() {
        return deliveringTotals;
    }

    public void setDeliveringTotals(Integer deliveringTotals) {
        this.deliveringTotals = deliveringTotals;
    }

    public Integer getTakeOffTotals() {
        return takeOffTotals;
    }

    public void setTakeOffTotals(Integer takeOffTotals) {
        this.takeOffTotals = takeOffTotals;
    }

    public Integer getHighRiskTotals() {
        return highRiskTotals;
    }

    public void setHighRiskTotals(Integer highRiskTotals) {
        this.highRiskTotals = highRiskTotals;
    }
}
