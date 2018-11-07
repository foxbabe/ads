package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class BaseMonitorStatisticViewModel {
    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "已播放次数")
    private Integer displayTimes;

    @ApiModelProperty(value = "展示总数")
    private Integer totalDisplayTimes;

    @ApiModelProperty(value = "完成比例")
    private String completeRatio;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "是否投放收银机")
    private boolean choseCashRegister;

    @ApiModelProperty(value = "投放平台")
    private String platform;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getTotalDisplayTimes() {
        return totalDisplayTimes;
    }

    public void setTotalDisplayTimes(Integer totalDisplayTimes) {
        this.totalDisplayTimes = totalDisplayTimes;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCompleteRatio() {
        return completeRatio;
    }

    public void setCompleteRatio(String completeRatio) {
        this.completeRatio = completeRatio;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isChoseCashRegister() {
        return choseCashRegister;
    }

    public void setChoseCashRegister(boolean choseCashRegister) {
        this.choseCashRegister = choseCashRegister;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
