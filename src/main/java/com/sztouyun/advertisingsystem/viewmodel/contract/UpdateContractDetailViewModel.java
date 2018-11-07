package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class UpdateContractDetailViewModel extends UpdateContractViewModel {

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同状态(0:全部 1:待提交 2:待审核 3:待签约 4:待执行 5:意外终止 6:执行完成)")
    private Integer contractStatus;

    @ApiModelProperty(value = "合同模板类型: 1: 收费模板  2: 免费模板  3: 通用模板")
    private Integer contractTemplateType;

    @ApiModelProperty(value = "合同模板更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date contractTemplateUpdatedTime;

    public Date getContractTemplateUpdatedTime() {
        return contractTemplateUpdatedTime;
    }

    public void setContractTemplateUpdatedTime(Date contractTemplateUpdatedTime) {
        this.contractTemplateUpdatedTime = contractTemplateUpdatedTime;
    }

    public Integer getContractTemplateType() {
        return contractTemplateType;
    }

    public void setContractTemplateType(Integer contractTemplateType) {
        this.contractTemplateType = contractTemplateType;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

}
