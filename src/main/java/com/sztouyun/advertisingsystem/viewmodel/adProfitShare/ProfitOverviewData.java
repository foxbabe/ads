package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

public class ProfitOverviewData {
    @ApiModelProperty(value = "日期")
    private Date selDate;
    @ApiModelProperty(value = "分成金额")
    private Double shareAmount;
    @ApiModelProperty(value = "已结算金额")
    private Double settledAmount;
    @ApiModelProperty(value = "分成占比")
    private String shareAmountPr;

    public Date getSelDate() {
        return selDate;
    }

    public void setSelDate(Date selDate) {
        this.selDate = selDate;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getShareAmountPr() {
        return shareAmountPr;
    }

    public void setShareAmountPr(String shareAmountPr) {
        this.shareAmountPr = shareAmountPr;
    }

    public Double getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Double settledAmount) {
        this.settledAmount = settledAmount;
    }
}
