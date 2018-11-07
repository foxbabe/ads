package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class ProfitOverviewViewModel {
    @ApiModelProperty(value = "时间周期：开始日期(yyyy年MM月dd日)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "时间周期：结束日期(yyyy年MM月dd日)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date endDate;
    private List<ProfitOverviewDataViewModel> profitOverviewDataList;


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ProfitOverviewDataViewModel> getProfitOverviewDataList() {
        return profitOverviewDataList;
    }

    public void setProfitOverviewDataList(List<ProfitOverviewDataViewModel> profitOverviewDataList) {
        this.profitOverviewDataList = profitOverviewDataList;
    }
}
