package com.sztouyun.advertisingsystem.viewmodel.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by szty on 2017/7/25.
 */
@ApiModel
public class UpdateCustomerViewModel extends CreateCustomerViewModel{

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String id;

    @ApiModelProperty(value = "客户编号")
    @Size(max = 128,message ="客户编号太长" )
    private String customerNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

}
