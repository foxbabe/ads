package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ProfitOverviewDataViewModel {
    @ApiModelProperty(value = "日期(yyyy-MM)")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date selDate;
    @ApiModelProperty(value = "分成金额")
    private String shareAmount;
    @ApiModelProperty(value = "已结算金额")
    private String settledAmount;
    @ApiModelProperty(value = "未结算金额")
    private String unsettledAmount;
    @ApiModelProperty(value = "分成占比")
    private String shareAmountPr;

    public Date getSelDate() {
        return selDate;
    }

    public void setSelDate(Date selDate) {
        this.selDate = selDate;
    }

    public String getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(String shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getShareAmountPr() {
        return shareAmountPr;
    }

    public void setShareAmountPr(String shareAmountPr) {
        this.shareAmountPr = shareAmountPr;
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
