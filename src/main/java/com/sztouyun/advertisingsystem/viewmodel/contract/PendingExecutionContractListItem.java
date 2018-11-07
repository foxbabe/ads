package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class PendingExecutionContractListItem {
    /**
     * 合同id
     */
    @ApiModelProperty(value = "合同id")
    private String id;

    /**
     * 合同编码
     */
    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "广告客户ID")
    private String customerId;

    /**
     * 广告客户名称
     */
    @ApiModelProperty(value = "广告客户名称")
    private String customerName;

    @ApiModelProperty(value = "开始投放时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date startTime;

    /**
     * 维护人
     */
    @ApiModelProperty(value = "维护人")
    private String ownerName;

    /**
     * 签约人
     */
    @ApiModelProperty(value = "签约人")
    private String signerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }
}
