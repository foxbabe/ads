package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class StoreProfitStatisticViewModel {

    @ApiModelProperty(value = "分成总收益")
    private String totalShareAmount;

    @ApiModelProperty(value = "时间周期-开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "时间周期-结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "投放广告数量")
    private Integer advertisementCount;

    public String getTotalShareAmount() {
        return totalShareAmount;
    }

    public void setTotalShareAmount(String totalShareAmount) {
        this.totalShareAmount = totalShareAmount;
    }

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

    public Integer getAdvertisementCount() {
        return advertisementCount;
    }

    public void setAdvertisementCount(Integer advertisementCount) {
        this.advertisementCount = advertisementCount;
    }
}
