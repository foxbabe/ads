package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementPeriodStoreProfitStatisticRequest {

    @ApiModelProperty(value = "门店ID")
    @NotBlank(message = "门店ID不能为空")
    private String storeId;

    @ApiModelProperty(value = "合同ID")
    @NotBlank(message = "广告ID不能为空")
    private String advertisementId;
}
