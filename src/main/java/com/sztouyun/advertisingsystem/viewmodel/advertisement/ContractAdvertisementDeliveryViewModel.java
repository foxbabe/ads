package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class ContractAdvertisementDeliveryViewModel {

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date endTime;

    @ApiModelProperty(value = "合同广告周期")
    private Integer advertisePeriod = 0;

    @ApiModelProperty(value = "已投放次数")
    private Long hasAdvertisedTimes = 0L;

    @ApiModelProperty(value = "已投放周期")
    private Integer hasAdvertisedPeriod = 0;

    @ApiModelProperty(value = "有效周期")
    private Integer effectivePeriod = 0;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
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

    public Integer getAdvertisePeriod() {
        return advertisePeriod;
    }

    public void setAdvertisePeriod(Integer advertisePeriod) {
        this.advertisePeriod = advertisePeriod;
    }

    public Long getHasAdvertisedTimes() {
        return hasAdvertisedTimes;
    }

    public void setHasAdvertisedTimes(Long hasAdvertisedTimes) {
        this.hasAdvertisedTimes = hasAdvertisedTimes;
    }

    public Integer getHasAdvertisedPeriod() {
        return hasAdvertisedPeriod;
    }

    public void setHasAdvertisedPeriod(Integer hasAdvertisedPeriod) {
        this.hasAdvertisedPeriod = hasAdvertisedPeriod;
    }

    public Integer getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(Integer effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }
}
