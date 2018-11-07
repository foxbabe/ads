package com.sztouyun.advertisingsystem.viewmodel.message.version;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateVersionInfoRequest extends CreateVersionInfoRequest {
    @NotNull(message = "版本信息ID不能为空")
    @ApiModelProperty(value = "版本信息ID")
    private String id;
}
