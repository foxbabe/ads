package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/10/18.
 */
@ApiModel
public class PositionTypeItemViewModel {
    @ApiModelProperty(value = "投放位置ID")
    private String   positionId;

    @ApiModelProperty(value = "投放位置")
    private String  positionName;

    @ApiModelProperty(value = "广告尺寸")
    private String positionSize;

    @ApiModelProperty(value = "尺寸类型，1：待机全屏广告，2：扫描支付页面，3：商家待机全屏，4：APP开屏，5：Banner")
    private Integer positionType;

    @ApiModelProperty(value = "终端类型，1：收银机，2：iOS,3：Android")
    private Integer terminalType;

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(String positionSize) {
        this.positionSize = positionSize;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }
}
