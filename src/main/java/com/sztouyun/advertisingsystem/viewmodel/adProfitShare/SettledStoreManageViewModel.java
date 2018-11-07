package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.util.Date;

@ApiModel
public class SettledStoreManageViewModel {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "结算门店数量")
    private Integer storeCount;
    @ApiModelProperty(value = "结算月份")
    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    private Date settledMonth;
    @ApiModelProperty(value = "结算总额")
    private String settledAmount;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date createdTime;
    @ApiModelProperty(value = "结算时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date settledTime;
    @ApiModelProperty(value = "结算人")
    private String creator;
    @ApiModelProperty(value = "结算状态")
    private Integer settleStatus;
    @ApiModelProperty(value = "结算状态名称")
    private String settleStatusName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Integer storeCount) {
        this.storeCount = storeCount;
    }

    public Date getSettledMonth() {
        return settledMonth;
    }

    public void setSettledMonth(Date settledMonth) {
        this.settledMonth = settledMonth;
    }

    public String getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(String settledAmount) {
        this.settledAmount = settledAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getSettleStatusName() {
        return settleStatusName;
    }

    public void setSettleStatusName(String settleStatusName) {
        this.settleStatusName = settleStatusName;
    }

    public Date getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(Date settledTime) {
        this.settledTime = settledTime;
    }
}
