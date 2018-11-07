package com.sztouyun.advertisingsystem.viewmodel.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel
public class BaseOpenApiViewModel {
    @ApiModelProperty(value = "合作方ID")
    @NotBlank(message = "合作方ID不能为空")
    private String partnerId;
    @ApiModelProperty(value = "三方ID")
    @NotBlank(message = "三方ID不能为空")
    private String thirdPartId;
}
