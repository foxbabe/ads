package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class ContractOperationViewModel {

    @ApiModelProperty(value = "合同Id", required = true)
    @NotBlank(message = "合同ID不能为空！")
    private String contractId;

    @ApiModelProperty(value = "操作（1：提交审批 ，2：审核，3：签约，5: 提交终止审核，6：终止审核", required = true)
    @EnumValue(enumClass = ContractOperationEnum.class,message = "操作类型不正确！")
    @NotNull(message = "操作为空！")
    private Integer operation;

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean successed;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000,message ="备注太长" )
    private String remark;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
