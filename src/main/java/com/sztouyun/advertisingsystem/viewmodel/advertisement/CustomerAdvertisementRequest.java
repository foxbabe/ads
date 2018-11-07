package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by wenfeng on 2017/9/7.
 */
public class CustomerAdvertisementRequest extends BasePageInfo {
    @ApiModelProperty(value = "客户ID)")
    @NotBlank(message = "客户ID不允许为空")
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
