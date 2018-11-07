package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BaseOpenApiViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wenfeng on 2018/1/29.
 */
@ApiModel
@Data
public class CreateMaterialRequest extends BaseOpenApiViewModel {
    @ApiModelProperty(value = "素材类型")
    @EnumValue(enumClass = MaterialTypeEnum.class)
    private Integer materialType;

    @ApiModelProperty(value = "广告位置")
    @EnumValue(enumClass = AdvertisementPositionCategoryEnum.class,message = "不支持此广告位置")
    private Integer advertisementPositionCategory;

    @ApiModelProperty(value = "第三方原始的素材URL")
    private String originalUrl;

}
