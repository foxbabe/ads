package com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class CustomerVisitRequest extends BasePageInfo{

    @ApiModelProperty(value = "客户ID",required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}