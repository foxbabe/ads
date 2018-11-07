package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/12/14.
 */
@ApiModel
public abstract class AbstractAdvertisementDeliveryConfigInfo {
    @ApiModelProperty(value = "positionID")
    private String positionId;
    @ApiModelProperty(value = "广告位置类型（1： 待机全屏广告，2：扫描支付页面，3：商家待机全屏，4：APP开屏，5：Banner）")
    private String positionType;
    @ApiModelProperty(value = "广告位置名称")
    private String positionTypeName;
    @ApiModelProperty(value = "广告尺寸")
    private String resolution;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getPositionTypeName() {
        return positionTypeName;
    }

    public void setPositionTypeName(String positionTypeName) {
        this.positionTypeName = positionTypeName;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
