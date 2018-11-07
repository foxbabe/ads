package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractAdvertisementPositionConfigItem {

    @ApiModelProperty(value = "广告投放位置; 1:待机全屏广告; 2:扫描支付页面; 3:商家待机全屏; 4:APP开屏; 5:Banner")
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "广告投放位置名称")
    private String advertisementPositionTypeName;

    @ApiModelProperty(value = "广告时长")
    private Integer duration;

    @ApiModelProperty(value = "广告时长单位枚举名称")
    private String durationUnitName;

    @ApiModelProperty(value = "展示次数")
    private Integer  displayTimes;

    @ApiModelProperty(value = "展示次数单位枚举名称")
    private String displayTimesUnitName;

    public Integer getAdvertisementPositionType() {
        return advertisementPositionType;
    }

    public void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    public String getAdvertisementPositionTypeName() {
        return advertisementPositionTypeName;
    }

    public void setAdvertisementPositionTypeName(String advertisementPositionTypeName) {
        this.advertisementPositionTypeName = advertisementPositionTypeName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationUnitName() {
        return durationUnitName;
    }

    public void setDurationUnitName(String durationUnitName) {
        this.durationUnitName = durationUnitName;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }

    public String getDisplayTimesUnitName() {
        return displayTimesUnitName;
    }

    public void setDisplayTimesUnitName(String displayTimesUnitName) {
        this.displayTimesUnitName = displayTimesUnitName;
    }
}
