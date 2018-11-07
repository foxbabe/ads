package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2018/1/29.
 */
@Data
@ApiModel
public class AuditMaterialRequest {
    @ApiModelProperty(value = "素材ID",required = true)
    @NotBlank(message = "素材ID不能为空")
    private String id;

    @ApiModelProperty(value = "审核操作，true:通过，false:拒绝",required = true)
    @NotNull(message = "不允许为NULL")
    private Boolean approve;

    @ApiModelProperty(value = "拒绝原因（主分类）")
    private String reasonId;

    @ApiModelProperty(value = "拒绝原因（子分类）")
    private String subReasonId;

    @ApiModelProperty(value = "审核备注")
    private String auditRemark;

}
