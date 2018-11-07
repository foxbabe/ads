package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PartnerAdvertisementRelatedInfo {

    @ApiModelProperty(value = "正在投放的广告数量")
    private Long deliveryQuantity;

    @ApiModelProperty(value = "有效广告数量")
    private Long validDeliveryQuantity;

    @ApiModelProperty(value = "累计投放广告总数量")
    private Long totalDeliveryQuantity;

    @ApiModelProperty(value = "合作方id")
    private String partnerId;
}
