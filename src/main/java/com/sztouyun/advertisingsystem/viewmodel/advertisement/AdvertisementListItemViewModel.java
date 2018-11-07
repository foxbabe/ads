package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class AdvertisementListItemViewModel {
    @ApiModelProperty(value = "广告ID", required = true)
    private String id;

    @ApiModelProperty(value = "广告名称", required = true)
    private String advertisementName;

    @ApiModelProperty(value = "广告类型",required=true)
    private String advertisementType;

    @ApiModelProperty(value = "客户ID",required=true)
    private String customerId;

    @ApiModelProperty(value = "投放客户",required=true)
    private String customerName;

    @ApiModelProperty(value = "合同ID",required=true)
    private String contractId;

    @ApiModelProperty(value = "广告合同",required=true)
    private String contractName;

    @ApiModelProperty(value = "广告状态",required=true)
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称",required=true)
    private String advertisementStatusName;

    @ApiModelProperty(value = "总价", required = true)
    private Double totalCost;

    @ApiModelProperty(value = "投放周期", required = true)
    private String period;

    @ApiModelProperty(value = "开始时间", required = true)
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "维护人", required = true)
    private String owner;

    @ApiModelProperty(value = "投放平台", required = true)
    private String terminalNames;

    @ApiModelProperty(value = "是否开启分成")
    private boolean enableProfitShare;

    @ApiModelProperty(value = "更新时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isEnableProfitShare() {
        return enableProfitShare;
    }

    public void setEnableProfitShare(boolean enableProfitShare) {
        this.enableProfitShare = enableProfitShare;
    }

    public String getTerminalNames() {
        return terminalNames;
    }

    public void setTerminalNames(String terminalNames) {
        this.terminalNames = terminalNames;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public String getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        this.advertisementType = advertisementType;
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

    public Integer getAdvertisementStatus() {
        return advertisementStatus;
    }

    public void setAdvertisementStatus(Integer advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }

    public String getAdvertisementStatusName() {
        return advertisementStatusName;
    }

    public void setAdvertisementStatusName(String advertisementStatusName) {
        this.advertisementStatusName = advertisementStatusName;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
