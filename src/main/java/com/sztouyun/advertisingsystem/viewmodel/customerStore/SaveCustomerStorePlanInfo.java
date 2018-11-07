package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@ApiModel
public class SaveCustomerStorePlanInfo {

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "临时选点ID")
    private String tempCustomerStorePlanId;

    @ApiModelProperty(value = "数据库保存的选点Id, 编辑功能必须传")
    private String oldCustomerStorePlanId = "";
}
