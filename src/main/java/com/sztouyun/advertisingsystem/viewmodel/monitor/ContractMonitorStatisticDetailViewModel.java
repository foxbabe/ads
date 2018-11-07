package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
public class ContractMonitorStatisticDetailViewModel extends ContractMonitorStatisticItem{
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "监控开始时间")
    private Date monitorStartTime;

    @ApiModelProperty(value = "监控结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date monitorFinishedTime;

    @ApiModelProperty(value = "启动监控广告名称")
    private String firstDeliveryAdvertisementName;

    @ApiModelProperty(value = "启动监控操作人")
    private String starter;

    @ApiModelProperty(value = "监控周期")
    private String monitorPeroid;

    @ApiModelProperty(value = "客户头像")
    private String headPortrait;

    @ApiModelProperty(value = "设备的对应广告展示次数和占比")
    private List<ContractMonitorDisplayTimesRatioViewModel> contractMonitorDisplayTimesRatioViewModels = new ArrayList<>();

    public List<ContractMonitorDisplayTimesRatioViewModel> getContractMonitorDisplayTimesRatioViewModels() {
        return contractMonitorDisplayTimesRatioViewModels;
    }

    public void setContractMonitorDisplayTimesRatioViewModels(List<ContractMonitorDisplayTimesRatioViewModel> contractMonitorDisplayTimesRatioViewModels) {
        this.contractMonitorDisplayTimesRatioViewModels = contractMonitorDisplayTimesRatioViewModels;
    }

    public Date getMonitorStartTime() {
        return monitorStartTime;
    }

    public void setMonitorStartTime(Date monitorStartTime) {
        this.monitorStartTime = monitorStartTime;
    }

    public Date getMonitorFinishedTime() {
        return monitorFinishedTime;
    }

    public void setMonitorFinishedTime(Date monitorFinishedTime) {
        this.monitorFinishedTime = monitorFinishedTime;
    }

    public String getFirstDeliveryAdvertisementName() {
        return firstDeliveryAdvertisementName;
    }

    public void setFirstDeliveryAdvertisementName(String firstDeliveryAdvertisementName) {
        this.firstDeliveryAdvertisementName = firstDeliveryAdvertisementName;
    }

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public String getMonitorPeroid() {
        return monitorPeroid;
    }

    public void setMonitorPeroid(String monitorPeroid) {
        this.monitorPeroid = monitorPeroid;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
