package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractMonitorStatisticItem extends BaseMonitorStatisticViewModel {
    @ApiModelProperty(value = "投放城市数")
    private Integer totalCityCount;

    @ApiModelProperty(value = "投放门店数")
    private Integer totalStoreCount;

    @ApiModelProperty(value = "监控状态")
    private Integer status;

    @ApiModelProperty(value = "监控状态名称")
    private String statusName;

    @ApiModelProperty(value = "投放记录数")
    private Integer deliveryTimes;

    public Integer getTotalCityCount() {
        return totalCityCount;
    }

    public void setTotalCityCount(Integer totalCityCount) {
        this.totalCityCount = totalCityCount;
    }

    public Integer getTotalStoreCount() {
        return totalStoreCount;
    }

    public void setTotalStoreCount(Integer totalStoreCount) {
        this.totalStoreCount = totalStoreCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getDeliveryTimes() {
        return deliveryTimes;
    }

    public void setDeliveryTimes(Integer deliveryTimes) {
        this.deliveryTimes = deliveryTimes;
    }

}
