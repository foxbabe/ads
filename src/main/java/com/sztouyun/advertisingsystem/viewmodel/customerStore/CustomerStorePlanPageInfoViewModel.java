package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomerStorePlanPageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "选点记录编号")
    private String code;

    @ApiModelProperty(value = "业务员姓名")
    private String ownerName;
}
