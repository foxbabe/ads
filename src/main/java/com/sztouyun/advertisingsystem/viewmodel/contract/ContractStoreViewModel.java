package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class ContractStoreViewModel {

    @ApiModelProperty(value = "门店id", required = true)
    @NotBlank(message = "门店id不能为空")
    private String storeId;

    @ApiModelProperty(value = "合同id", required = true)
    @NotBlank(message = "合同id不能为空")
    private String contractId;


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getStoreId() {

        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
