package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractAdvertisementPositionConfigViewModel {

    @ApiModelProperty(value = "广告投放位置; 1:待机全屏广告; 2:扫描支付页面; 3:商家待机全屏; 4:APP开屏; 5:Banner")
    @EnumValue(enumClass = AdvertisementPositionTypeEnum.class, message = "广告投放位置类型不正确")
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "终端类型; 1:收银机; 2:IOS; 3:Android")
    @EnumValue(enumClass = TerminalTypeEnum.class, message = "终端类型不正确")
    private Integer terminalType;

    @ApiModelProperty(value = "系统参数广告位置配置ID")
    private String systemParamAdvertisementPositionId;

    @ApiModelProperty(value = "广告时长配置ID")
    private String advertisementDurationConfigId;

    @ApiModelProperty(value = "广告展示次数配置ID")
    private String advertisementDisplayTimesConfigId;

    public Integer getAdvertisementPositionType() {
        return advertisementPositionType;
    }

    public void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public String getAdvertisementDurationConfigId() {
        return advertisementDurationConfigId;
    }

    public void setAdvertisementDurationConfigId(String advertisementDurationConfigId) {
        this.advertisementDurationConfigId = advertisementDurationConfigId;
    }

    public String getAdvertisementDisplayTimesConfigId() {
        return advertisementDisplayTimesConfigId;
    }

    public void setAdvertisementDisplayTimesConfigId(String advertisementDisplayTimesConfigId) {
        this.advertisementDisplayTimesConfigId = advertisementDisplayTimesConfigId;
    }

    public String getSystemParamAdvertisementPositionId() {
        return systemParamAdvertisementPositionId;
    }

    public void setSystemParamAdvertisementPositionId(String systemParamAdvertisementPositionId) {
        this.systemParamAdvertisementPositionId = systemParamAdvertisementPositionId;
    }
}
