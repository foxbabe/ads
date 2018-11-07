package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@ApiModel
@Data
public class SubAuditReasonConfigViewModel {
    @ApiModelProperty(value = "子分类", required = true)
    @NotBlank(message = "子分类不能为空")
    @Size(max = 50,message = "子分类太长")
    private String name;

    @ApiModelProperty(value = "问题分类Id", required = true)
    @NotBlank(message = "问题分类Id不能为空")
    private String parentId;
}
