package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.partner.advertisement.store.PartnerAdvertisementDeliveryRecordStatusEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.AreaRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class PartnerAdvertisementStoreListRequest extends AreaRequest {

    @ApiModelProperty(value = "广告ID",required = true)
    @NotBlank(message = "广告ID不能为空")
    private String partnerAdvertisementId;

    @ApiModelProperty(value = "投放位置")
    @EnumValue(enumClass = AdvertisementPositionTypeEnum.class,message = "广告投放位置不匹配",nullable = true)
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "门店广告状态")
    @EnumValue(enumClass = PartnerAdvertisementDeliveryRecordStatusEnum.class,message = "门店广告状态不匹配",nullable = true)
    private Integer advertisementStoreStatus;

    @ApiModelProperty(value = "是否有效")
    private Boolean valid;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

}
