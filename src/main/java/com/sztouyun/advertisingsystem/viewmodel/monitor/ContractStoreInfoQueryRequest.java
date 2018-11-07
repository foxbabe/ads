package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.viewmodel.AreaRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class ContractStoreInfoQueryRequest extends AreaRequest {
    @ApiModelProperty(value = "合同ID")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;

    @ApiModelProperty(value = "门店是否可用(全部:null,可用：true,不可用：false)")
    private Boolean available;

    private String advertisementId;
}
