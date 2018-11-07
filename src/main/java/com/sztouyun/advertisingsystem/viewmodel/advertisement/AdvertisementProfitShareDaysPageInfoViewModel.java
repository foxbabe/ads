package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModelProperty;

public class AdvertisementProfitShareDaysPageInfoViewModel {

    @ApiModelProperty(value = "广告ID", required = true)
    private String id;

    @ApiModelProperty(value = "广告名称", required = true)
    private String advertisementName;

    @ApiModelProperty(value = "广告类型",required=true)
    private String advertisementType;

    @ApiModelProperty(value = "广告状态名称",required=true)
    private String advertisementStatusName;

    @ApiModelProperty(value = "是否激活")
    private boolean active;

    @ApiModelProperty(value = "是否开启分成", required = true)
    private boolean enableProfitShare;

    @ApiModelProperty(value = "单日收益金额", required = true)
    private String shareAmountDay;

    @ApiModelProperty(value = "收益占比")
    private String shareAmountDayRatio;

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

    public boolean isEnableProfitShare() {
        return enableProfitShare;
    }

    public void setEnableProfitShare(boolean enableProfitShare) {
        this.enableProfitShare = enableProfitShare;
    }

    public String getShareAmountDay() {
        return shareAmountDay;
    }

    public void setShareAmountDay(String shareAmountDay) {
        this.shareAmountDay = shareAmountDay;
    }

    public String getShareAmountDayRatio() {
        return shareAmountDayRatio;
    }

    public void setShareAmountDayRatio(String shareAmountDayRatio) {
        this.shareAmountDayRatio = shareAmountDayRatio;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

