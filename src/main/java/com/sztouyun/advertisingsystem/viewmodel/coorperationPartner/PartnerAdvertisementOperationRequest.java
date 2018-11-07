package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 *
 */
@Data
@ApiModel
public class PartnerAdvertisementOperationRequest {

    @ApiModelProperty(value = "广告ID",required = true)
    @NotBlank(message = "广告ID不能为空")
    private String partnerAdvertisementId;

    @ApiModelProperty(value = "操作备注")
    @Size(max = 2000, message = "备注太长")
    @NotBlank(message = "备注不能为空")
    private String remark;

}
