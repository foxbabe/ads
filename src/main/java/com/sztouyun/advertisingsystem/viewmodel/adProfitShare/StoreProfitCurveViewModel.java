package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel
public class StoreProfitCurveViewModel {
    @ApiModelProperty(value = "分成总金额")
    private String totalShareAmount;

    private List<StoreProfitCurveData> profitList;

    public String getTotalShareAmount() {
        return totalShareAmount;
    }

    public void setTotalShareAmount(String totalShareAmount) {
        this.totalShareAmount = totalShareAmount;
    }

    public List<StoreProfitCurveData> getProfitList() {
        return profitList;
    }

    public void setProfitList(List<StoreProfitCurveData> profitList) {
        this.profitList = profitList;
    }
}
