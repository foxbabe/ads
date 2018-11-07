package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class AdvertisementProfitShareDaysPageInfoRequest extends BasePageInfo {
    @ApiModelProperty(value = "门店收益统计表ID")
    private String storeProfitStatisticId;

    @ApiModelProperty(value = "广告名称)")
    private String advertisementName;

    @ApiModelProperty(value = "操作状态(4:投放中,5:已下架,6:广告完成),逗号隔开")
    @NotBlank(message = "广告状态类型不能为空")
    private String profitShareStatus;

    @ApiModelProperty(value = "是否激活,全选传Null")
    private Boolean active;

    @ApiModelProperty(value = "是否开启广告分成", required = true)
    private Boolean enableProfitShare;

    public String getStoreProfitStatisticId() {
        return storeProfitStatisticId;
    }

    public void setStoreProfitStatisticId(String storeProfitStatisticId) {
        this.storeProfitStatisticId = storeProfitStatisticId;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public String getProfitShareStatus() {
        return profitShareStatus;
    }

    public void setProfitShareStatus(String profitShareStatus) {
        this.profitShareStatus = profitShareStatus;
    }

    public Boolean getEnableProfitShare() {
        return enableProfitShare;
    }

    public void setEnableProfitShare(Boolean enableProfitShare) {
        this.enableProfitShare = enableProfitShare;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

