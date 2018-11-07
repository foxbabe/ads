package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ContractPlatformInfo {

    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty(value = "终端类型")
    private Integer terminalType;
}
