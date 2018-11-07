package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2018/1/29.
 */
@ApiModel
public class ProfitStatisticViewModel {
    @ApiModelProperty(value = "系统分成总额")
    private String totalShareAmount;
    @ApiModelProperty(value = "已结算总额")
    private String settledAmount;
    @ApiModelProperty(value = "未结算总额")
    private String unsettledAmount;

    public String getTotalShareAmount() {
        return totalShareAmount;
    }

    public void setTotalShareAmount(String totalShareAmount) {
        this.totalShareAmount = totalShareAmount;
    }

    public String getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(String settledAmount) {
        this.settledAmount = settledAmount;
    }

    public String getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(String unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }
}
