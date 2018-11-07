package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
@NoArgsConstructor
public class BaseAdvertisementSettlementRequest {

    @ApiModelProperty(value = "广告ID", required = true)
    @NotBlank(message = "广告ID不能为空")
    private String advertisementId;

    @ApiModelProperty(value = "结算ID", required = true)
    @NotBlank(message = "结算ID不能为空")
    private String settlementId;

    public BaseAdvertisementSettlementRequest(String advertisementId, String settlementId) {
        this.advertisementId = advertisementId;
        this.settlementId = settlementId;
    }
}
