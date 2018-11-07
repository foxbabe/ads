package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class AdvertisementProfitSharePageInfoViewModel {
    @ApiModelProperty(value = "广告ID", required = true)
    private String id;

    @ApiModelProperty(value = "广告名称", required = true)
    private String advertisementName;

    @ApiModelProperty(value = "投放平台", required = true)
    private String terminalNames;

    @ApiModelProperty(value = "广告类型",required=true)
    private String advertisementType;

    @ApiModelProperty(value = "广告状态名称",required=true)
    private String advertisementStatusName;

    @ApiModelProperty(value = "投放时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "实际已投放天数", required = true)
    private String period;

    @ApiModelProperty(value = "是否开启分成", required = true)
    private boolean enableProfitShare;

    @ApiModelProperty(value = "分成金额", required = true)
    private String shareAmount;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "能否查看")
    private boolean canView;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public String getTerminalNames() {
        return terminalNames;
    }

    public void setTerminalNames(String terminalNames) {
        this.terminalNames = terminalNames;
    }

    public String getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        this.advertisementType = advertisementType;
    }

    public String getAdvertisementStatusName() {
        return advertisementStatusName;
    }

    public void setAdvertisementStatusName(String advertisementStatusName) {
        this.advertisementStatusName = advertisementStatusName;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isEnableProfitShare() {
        return enableProfitShare;
    }

    public void setEnableProfitShare(boolean enableProfitShare) {
        this.enableProfitShare = enableProfitShare;
    }

    public String getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(String shareAmount) {
        this.shareAmount = shareAmount;
    }
}
