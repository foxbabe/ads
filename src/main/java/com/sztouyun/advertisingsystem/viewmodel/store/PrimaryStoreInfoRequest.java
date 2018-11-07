package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class PrimaryStoreInfoRequest {

    @ApiModelProperty(value = "门店ID",required = true)
    @NotBlank(message = "门店ID不能为空")
    private String storeId;

    @ApiModelProperty(value = "合同Id")
    private String contractId;

    @ApiModelProperty(value = "客户选点Id")
    private String customerStorePlanId;

}
