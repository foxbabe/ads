package com.sztouyun.advertisingsystem.viewmodel.message.version;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class CreateVersionInfoRequest {
    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号")
    private String versionNumber;

    @NotNull(message = "版本内容不能为空")
    @ApiModelProperty(value = "版本内容")
    private String versionContent;

    @NotNull(message = "是否弹窗字段不能为空")
    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean isTip;
}
