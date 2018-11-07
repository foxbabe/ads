package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wenfeng on 2018/1/31.
 */
@ApiModel
@Data
public class PartnerMaterialDetailViewModel {
    @ApiModelProperty(value = "素材url")
    private String url;
    @ApiModelProperty(value = "审核备注")
    private String auditRemark;
    @ApiModelProperty(value = "拒绝原因（主分类）")
    private String reason;
    @ApiModelProperty(value = "拒绝原因（子分类）")
    private String subReason;
}
