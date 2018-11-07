package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@ApiModel
@Data
public class UpdateAuditReasonConfigViewModel {
    @ApiModelProperty(value = "问题分类id", required = true)
    @NotBlank(message = "问题分类id不能为空")
    private String parentId;

    @ApiModelProperty(value = "问题分类名称")
    @Size(max = 20,message = "问题分类名称太长")
    private String parentName;

    @ApiModelProperty(value = "子分类id")
    private String subId;

    @ApiModelProperty(value = "子分类名称")
    @Size(max = 50,message = "子分类名称太长")
    private String subName;
}
