package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by szty on 2017/8/2.
 */
@ApiModel
public class ContractListViewModel {

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

    /**
     * 广告客户名称
     */
    @ApiModelProperty(value = "广告客户名称")
    private String customerName;

    /**
     * 维护人
     */
    @ApiModelProperty(value = "维护人")
    private String ownerName;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    /**
     * 签约人
     */
    @ApiModelProperty(value = "签约人")
    private String signerName;

    /**
     * 合同状态名称
     */
    @ApiModelProperty(value = "合同状态名称")
    private String contractStatuName;

    /**
     * 合同状态
     */
    @ApiModelProperty(value = "合同状态")
    private Integer contractStatus;

    /**
     * 签约时间
     */
    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date signTime;

    @ApiModelProperty(value = "合同创建时间, 时间格式为'yyyy年MM月dd日'")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "是否审核中")
    private boolean auditing;

    @ApiModelProperty(value = "提交审核说明")
    private String auditingRemark;

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getContractStatuName() {
        return contractStatuName;
    }

    public void setContractStatuName(String contractStatuName) {
        this.contractStatuName = contractStatuName;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isAuditing() {
        return auditing;
    }

    public void setAuditing(boolean auditing) {
        this.auditing = auditing;
    }

    public String getAuditingRemark() {
        return auditingRemark;
    }

    public void setAuditingRemark(String auditingRemark) {
        this.auditingRemark = auditingRemark;
    }
}
