package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class PartnerAdvertisementMaterialListRequest extends BasePageInfo {

    @ApiModelProperty(value = "广告ID",required = true)
    @NotBlank(message = "广告ID不能为空")
    private String partnerAdvertisementId;

}
