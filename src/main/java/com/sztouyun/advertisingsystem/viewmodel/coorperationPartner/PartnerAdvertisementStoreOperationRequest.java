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
public class PartnerAdvertisementStoreOperationRequest {

    @ApiModelProperty(value = "门店ID",required = true)
    @NotBlank(message = "ID不能为空")
    private String partnerAdvertisementStoreId;

    @ApiModelProperty(value = "操作备注")
    @Size(max = 2000, message = "备注太长")
    @NotBlank(message = "备注不能为空")
    private String remark;

}
