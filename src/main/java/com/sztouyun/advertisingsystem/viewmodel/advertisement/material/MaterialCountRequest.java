package com.sztouyun.advertisingsystem.viewmodel.advertisement.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class MaterialCountRequest {

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @ApiModelProperty(value = "投放位置ID")
    private String positionId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
