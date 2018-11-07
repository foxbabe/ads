package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractMonitorDisplayTimesRatioViewModel {

    @ApiModelProperty("设备类型 1:收银机 2:iOS 3:Android")
    private Integer TerminalType;

    @ApiModelProperty("已经展示次数")
    private Integer hasDisplayTimes;

    @ApiModelProperty("占比")
    private String completeRatio;


    public Integer getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(Integer terminalType) {
        TerminalType = terminalType;
    }

    public Integer getHasDisplayTimes() {
        return hasDisplayTimes;
    }

    public void setHasDisplayTimes(Integer hasDisplayTimes) {
        this.hasDisplayTimes = hasDisplayTimes;
    }

    public String getCompleteRatio() {
        return completeRatio;
    }

    public void setCompleteRatio(String completeRatio) {
        this.completeRatio = completeRatio;
    }
}
