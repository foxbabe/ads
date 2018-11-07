package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractMonitorStatisticRequest extends BasePageInfo{
    @ApiModelProperty(value = "业务员名称")
    private String nickname;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "监控状态(0:全部，1：监控中，2：已完成)",required = true)
    @EnumValue(enumClass = MonitorStatusEnum.class,nullable = false)
    private Integer status;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
}
