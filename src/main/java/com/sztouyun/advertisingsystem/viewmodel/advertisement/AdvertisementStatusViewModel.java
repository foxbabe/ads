package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by szty on 2017/8/3.
 */
public class AdvertisementStatusViewModel implements Serializable{

    @ApiModelProperty(hidden = true)
    private Integer advertisementStatus;

    @ApiModelProperty(hidden = true)
    private Long statusCount;

    @ApiModelProperty(value = "全部广告")
    private Long total=0L;

    @ApiModelProperty(value = "待审核")
    private Long pendingAuditing=0L;

    @ApiModelProperty(value = "待投放")
    private Long pendingDelivery=0L;

    @ApiModelProperty(value = "投放中")
    private Long deliverying=0L;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPendingAuditing() {
        return pendingAuditing;
    }

    public void setPendingAuditing(Long pendingAuditing) {
        this.pendingAuditing = pendingAuditing;
    }

    public Long getPendingDelivery() {
        return pendingDelivery;
    }

    public void setPendingDelivery(Long pendingDelivery) {
        this.pendingDelivery = pendingDelivery;
    }

    public Long getDeliverying() {
        return deliverying;
    }

    public void setDeliverying(Long deliverying) {
        this.deliverying = deliverying;
    }

    public Integer getAdvertisementStatus() {
        return advertisementStatus;
    }

    public void setAdvertisementStatus(Integer advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }

    public Long getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(Long statusCount) {
        this.statusCount = statusCount;
    }
}
