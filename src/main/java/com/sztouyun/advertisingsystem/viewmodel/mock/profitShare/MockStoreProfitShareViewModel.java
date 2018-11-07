package com.sztouyun.advertisingsystem.viewmodel.mock.profitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel
public class MockStoreProfitShareViewModel {

    @ApiModelProperty(value = "门店信息ID", required = true)
    private List<String> storeIds;

    @ApiModelProperty(value = "计算日期--开始时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "计算日期--结束时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "每天开机时长", required = true)
    private Double bootTime;

    @ApiModelProperty(value = "当日门店月平均订单数量", required = true)
    private Double orderCount;

    @ApiModelProperty(value = "具有上报日志的广告", required = true)
    List<String> activeAdvertisementIds;

    public Double getBootTime() {
        return bootTime;
    }

    public void setBootTime(Double bootTime) {
        this.bootTime = bootTime;
    }

    public List<String> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<String> storeIds) {
        this.storeIds = storeIds;
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

    public Double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Double orderCount) {
        this.orderCount = orderCount;
    }

    public List<String> getActiveAdvertisementIds() {
        return activeAdvertisementIds;
    }

    public void setActiveAdvertisementIds(List<String> activeAdvertisementIds) {
        this.activeAdvertisementIds = activeAdvertisementIds;
    }
}
