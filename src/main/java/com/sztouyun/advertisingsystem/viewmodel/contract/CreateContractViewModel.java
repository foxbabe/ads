package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@ApiModel
public class CreateContractViewModel extends BaseContractViewModel {

    @ApiModelProperty(value = "合同编码", required = true)
    @Size(max = 128,message ="合同编码太长" )
    @NotBlank(message = "合同编码不能为空")
    private String contractCode;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
}
