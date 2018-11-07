package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class AdvertisementProfitSharePageInfoRequest extends BasePageInfo {

    @ApiModelProperty(value = "门店ID)")
    @NotBlank(message = "门店ID不能为空")
    private String storeId;

    @ApiModelProperty(value = "广告名称)")
    private String advertisementName;

    @ApiModelProperty(value = "操作状态(4:投放中,5:已下架,6:广告完成),逗号隔开")
    @NotBlank(message = "广告状态类型不能为空")
    private String profitShareStatus;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value="结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "是否开启广告分成", required = true)
    private Boolean enableProfitShare;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
