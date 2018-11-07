package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by szty on 2018/6/13.
 */
@Data
@ApiModel
public class InvalidCustomerStorePlanDetailRequest extends BasePageInfo {
    @ApiModelProperty(value = "临时客户选点ID")
    @NotBlank(message = "临时客户选点ID不能为空")
    private String tempCustomerStorePlanId;
}
