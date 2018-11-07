package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;

public class CustomerQueryRequest extends BasePageInfo {
    @ApiModelProperty(value = "广告客户名称", required = true)
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
