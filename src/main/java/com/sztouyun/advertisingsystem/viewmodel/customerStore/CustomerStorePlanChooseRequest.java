package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class CustomerStorePlanChooseRequest {

    @ApiModelProperty(value = "选点记录id", required = true)
    @NotBlank(message = "选点记录id不能为空")
    private String customerStorePlanId;

    @ApiModelProperty(value = "合同Id", required = true)
    @NotBlank(message = "合同Id不能为空")
    private String contractId;

}
