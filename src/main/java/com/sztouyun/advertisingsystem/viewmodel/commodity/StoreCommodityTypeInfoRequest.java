package com.sztouyun.advertisingsystem.viewmodel.commodity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class StoreCommodityTypeInfoRequest{

    @ApiModelProperty(value = "门店Id",required = true)
    @NotBlank(message = "门店Id不能为空")
    private String storeId;

    @ApiModelProperty(value = "供应商Id")
    @NotNull(message = "供应商Id不能为空")
    private Long supplierId;

}
