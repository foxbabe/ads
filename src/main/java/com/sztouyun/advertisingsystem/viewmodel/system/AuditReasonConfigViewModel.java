package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@ApiModel
@Data
public class AuditReasonConfigViewModel {

    @ApiModelProperty(value = "问题分类名称", required = true)
    @NotBlank(message = "问题分类名称不能为空")
    @Size(max = 20,message = "问题分类名称太长")
    private String name;


//    @ApiModelProperty(value = "子分类")
//    @NotBlank(message = "子分类不能为空")
//    @Size(max = 50,message = "子分类太长")
//    private String subClassName;

}
