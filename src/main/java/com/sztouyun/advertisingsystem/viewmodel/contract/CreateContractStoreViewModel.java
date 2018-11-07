package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class CreateContractStoreViewModel extends ContractStoreViewModel {

    @ApiModelProperty(value = "门店类型", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "门店类型最大值不能超过999999999")
    @NotNull(message = "门店类型不能为空")
    private Integer storeType;

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }
}
