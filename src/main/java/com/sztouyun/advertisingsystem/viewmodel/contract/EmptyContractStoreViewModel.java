package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class EmptyContractStoreViewModel {

    @ApiModelProperty(value = "门店类型", required = true)
    @NotNull(message = "门店类型不能为空")
    private Integer storeType;

    @ApiModelProperty(value = "合同id", required = true)
    @NotBlank(message = "合同id不能为空")
    private String contractId;


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }
}
