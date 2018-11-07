package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by szty on 2017/8/3.
 */
public class ContractStatusViewModel implements Serializable{

    private Integer contractStatus;

    private Long statusCount;

    @ApiModelProperty(value = "全部合同")
    private Long total;

    @ApiModelProperty(value = "待审核")
    private Long toAudit;

    @ApiModelProperty(value = "待签约")
    private Long toSign;

    @ApiModelProperty(value = "待执行")
    private Long toExecute;

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Long getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(Long statusCount) {
        this.statusCount = statusCount;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getToAudit() {
        return toAudit == null? 0:toAudit;
    }

    public void setToAudit(Long toAudit) {
        this.toAudit = toAudit;
    }

    public Long getToSign() {
        return toSign == null? 0:toSign;
    }

    public void setToSign(Long toSign) {
        this.toSign = toSign;
    }

    public Long getToExecute() {
        return toExecute == null? 0:toExecute;
    }

    public void setToExecute(Long toExecute) {
        this.toExecute = toExecute;
    }
}
