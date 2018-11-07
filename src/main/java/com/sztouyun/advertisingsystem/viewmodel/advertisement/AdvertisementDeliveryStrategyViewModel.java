package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class AdvertisementDeliveryStrategyViewModel {

    @ApiModelProperty(value = "投放策略ID")
    private String id;

    @ApiModelProperty(value = "是否强制每天展示次数播放")
    private Boolean controlDisplayTimes;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "修改人")
    private String updater;

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getControlDisplayTimes() {
        return controlDisplayTimes;
    }

    public void setControlDisplayTimes(Boolean controlDisplayTimes) {
        this.controlDisplayTimes = controlDisplayTimes;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
