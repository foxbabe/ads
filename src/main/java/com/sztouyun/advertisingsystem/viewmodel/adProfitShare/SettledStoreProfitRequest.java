package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by wenfeng on 2018/1/17.
 */
public class SettledStoreProfitRequest{
    @ApiModelProperty(value = "结算ID",required = false)
    private String id;

    @ApiModelProperty(value = "月流水ID",required = true)
    private String periodStoreProfitStatisticId;


    @ApiModelProperty(value = "计算月份",required = true)
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date settledMonth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriodStoreProfitStatisticId() {
        return periodStoreProfitStatisticId;
    }

    public void setPeriodStoreProfitStatisticId(String periodStoreProfitStatisticId) {
        this.periodStoreProfitStatisticId = periodStoreProfitStatisticId;
    }

    public Date getSettledMonth() {
        return settledMonth;
    }

    public void setSettledMonth(Date settledMonth) {
        this.settledMonth = settledMonth;
    }
}
