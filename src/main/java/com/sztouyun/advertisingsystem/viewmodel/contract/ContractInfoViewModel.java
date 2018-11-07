package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class ContractInfoViewModel {
    @ApiModelProperty(value = "合同ID")
    private String id;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "开始时间, 时间格式为'yyyy年MM月dd日'")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间, 时间格式为'yyyy年MM月dd日'")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "负责人")
    private String owner;

    @ApiModelProperty(value = "总金额")
    private Double totalCost;

    @ApiModelProperty(value = "能否查看")
    private boolean canView;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }
}
