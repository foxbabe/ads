package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AdvertisementPageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "广告名称)")
    private String advertisementName;

    @ApiModelProperty(value = "广告客户名称")
    private String customerName;

    @ApiModelProperty(value = "广告合同名称")
    private String contractName;

    @ApiModelProperty(hidden = true)
    private JoinDescriptor joinDescriptor;

    @ApiModelProperty(value = "广告状态(0:全部 2:待审核 3:待投放 4:投放中)")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "是否高风险广告; true: 是, false: 不是; 同时advertisementStatus必须传入投放中的值")
    private Boolean isHighRisk = false;

    public Boolean getHighRisk() {
        return isHighRisk;
    }

    public void setHighRisk(Boolean highRisk) {
        isHighRisk = highRisk;
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

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public JoinDescriptor getJoinDescriptor() {
        return joinDescriptor;
    }

    public void setJoinDescriptor(JoinDescriptor joinDescriptor) {
        this.joinDescriptor = joinDescriptor;
    }

    public Integer getAdvertisementStatus() {
        if (advertisementStatus == null) {
            advertisementStatus = 0;
        }
        return advertisementStatus;
    }

    public void setAdvertisementStatus(Integer advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }
}
